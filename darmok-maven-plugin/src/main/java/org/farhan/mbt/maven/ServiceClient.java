package org.farhan.mbt.maven;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ServiceClient {

	private final Log log;
	private final RestTemplate restTemplate;
	private final String host;
	private final int port;
	private final int timeout;
	private static final int RETRY_COUNT = 60;

	public ServiceClient(Log log, String host, int port, int timeout) {
		this.log = log;
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		RestClientConfig config = new RestClientConfig();
		this.restTemplate = config.restTemplate();
	}

	private String getBaseUrl() {
		return "http://" + host + ":" + port + "/";
	}

	private <T> T retry(Supplier<T> action, String operation) throws Exception {
		int retryCount = 0;
		while (retryCount < RETRY_COUNT) {
			try {
				return action.get();
			} catch (Exception e) {
				retryCount++;
				log.error("Retry attempt " + retryCount + " for " + operation, e);
				if (retryCount >= RETRY_COUNT) {
					throw new Exception("Max retries reached while " + operation, e);
				}
				Thread.sleep(1000);
			}
		}
		throw new Exception("Max retries reached while " + operation);
	}

	public void waitForService() throws MojoExecutionException {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < timeout) {
			try {
				ResponseEntity<String> response = restTemplate.getForEntity(
						getBaseUrl() + "actuator/health", String.class);
				if (response.getStatusCode() == HttpStatus.OK
						&& response.getBody().contains("\"status\":\"UP\"")) {
					log.info("Service ready");
					return;
				}
			} catch (Exception e) {
				long timeLeft = (timeout - (System.currentTimeMillis() - startTime)) / 1000;
				log.info("Service not ready yet, " + timeLeft + " seconds remaining");
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new MojoExecutionException("Interrupted while waiting for service", e);
			}
		}
		throw new MojoExecutionException(
				"Service did not become available within " + timeout + " milliseconds");
	}

	public void clearObjects(String resource, String goal, String tags) throws Exception {
		TreeMap<String, String> parameters = new TreeMap<>();
		String url = getBaseUrl() + resource + "/clear" + goal + "Objects";
		if (tags != null && !tags.isEmpty()) {
			parameters.put("tags", tags);
			url += "?tags={tags}";
		}
		String finalUrl = url;
		retry(() -> {
			restTemplate.exchange(finalUrl, HttpMethod.DELETE,
					new HttpEntity<>(new HttpHeaders()), Void.class, parameters);
			return null;
		}, "clearing objects for goal: " + goal);
	}

	public String convertObject(String resource, String goal, String tags,
			String fileName, String contents) throws Exception {
		TreeMap<String, String> parameters = new TreeMap<>();
		String url = getBaseUrl() + resource + "/run" + goal + "?fileName={fileName}";
		if (tags != null && !tags.isEmpty()) {
			parameters.put("tags", tags);
			url += "&tags={tags}";
		}
		parameters.put("fileName", fileName);
		String finalUrl = url;
		return retry(() -> {
			ResponseEntity<TransformableFile> postResponse = restTemplate.exchange(
					finalUrl, HttpMethod.POST,
					new HttpEntity<>(contents, new HttpHeaders()),
					TransformableFile.class, parameters);
			return postResponse.getBody().getFileContent();
		}, "converting object for goal: " + goal);
	}

	public List<TransformableFile> getObjectNames(String resource, String goal,
			String tags) throws Exception {
		TreeMap<String, String> parameters = new TreeMap<>();
		String url = getBaseUrl() + resource + "/get" + goal + "ObjectNames";
		if (tags != null && !tags.isEmpty()) {
			parameters.put("tags", tags);
			url += "?tags={tags}";
		}
		String finalUrl = url;
		return retry(() -> {
			ResponseEntity<List<TransformableFile>> response = restTemplate.exchange(
					finalUrl, HttpMethod.GET,
					new HttpEntity<>(new HttpHeaders()),
					new ParameterizedTypeReference<List<TransformableFile>>() {},
					parameters);
			List<TransformableFile> fileList = response.getBody();
			for (TransformableFile tf : fileList) {
				log.info("ObjectName: " + tf.getFileName());
			}
			return fileList;
		}, "getting object names for goal: " + goal);
	}

	public void executeToUML(String resource, String goal, String tags, String baseDir,
			String extension) throws Exception {
		waitForService();
		clearObjects(resource, goal, tags);

		String[] dirs = { "src/test/resources/asciidoc/", "src-gen/test/resources/cucumber/" };
		if (!baseDir.endsWith("/")) {
			baseDir += "/";
		}
		for (String dir : dirs) {
			ArrayList<String> stepdefs = new ArrayList<>();
			File aDir = new File(baseDir + dir);
			List<String> fileNames = listFiles(aDir, dir, extension);
			for (String fileName : fileNames) {
				if (fileName.startsWith(dir + "stepdefs")) {
					stepdefs.add(fileName);
				} else {
					log.info("Converting: " + fileName);
					String content = Files.readString(
							Path.of(baseDir, fileName), StandardCharsets.UTF_8);
					convertObject(resource, goal, tags, fileName, content);
				}
			}
			for (String fileName : stepdefs) {
				log.info("Converting: " + fileName);
				String content = Files.readString(
						Path.of(baseDir, fileName), StandardCharsets.UTF_8);
				convertObject(resource, goal, tags, fileName, content);
			}
		}
	}

	public void executeFromUML(String resource, String goal, String tags,
			String baseDir) throws Exception {
		waitForService();
		if (!baseDir.endsWith("/")) {
			baseDir += "/";
		}
		for (TransformableFile tf : getObjectNames(resource, goal, tags)) {
			Path filePath = Path.of(baseDir, tf.getFileName());
			String existingContent = Files.exists(filePath)
					? Files.readString(filePath, StandardCharsets.UTF_8) : null;
			String content = convertObject(resource, goal, tags,
					tf.getFileName(), existingContent);
			if (content != null && !content.isEmpty()) {
				log.info("Converting: " + tf.getFileName());
				Files.createDirectories(filePath.getParent());
				Files.writeString(filePath, content, StandardCharsets.UTF_8);
			}
		}
	}

	private List<String> listFiles(File dir, String relativePath, String extension) {
		List<String> files = new ArrayList<>();
		if (!dir.exists()) {
			return files;
		}
		for (String name : dir.list()) {
			String path = relativePath + name;
			File file = new File(dir, name);
			if (file.isDirectory()) {
				files.addAll(listFiles(file, path + "/", extension));
			} else if (path.toLowerCase().endsWith(extension.toLowerCase())) {
				files.add(path);
			}
		}
		return files;
	}
}
