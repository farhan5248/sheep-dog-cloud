package org.farhan.mbt.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class MBTMojo extends AbstractMojo {

	/**
	 * The Maven Project.
	 */
	@Parameter(defaultValue = "${project}", readonly = true)
	public MavenProject project;

	public String baseDir = "";

	private final RestTemplate restTemplate;
	private final int RETRY_COUNT = 10;

	public MBTMojo() {
		baseDir = new File("").getAbsolutePath();
		RestClientConfig config = new RestClientConfig();
		this.restTemplate = config.restTemplate();
	}

	@Parameter(property = "tags", defaultValue = "")
	public String tags;

	@Parameter(property = "host", defaultValue = "sheepdogdev.io")
	public String host;

	@Parameter(property = "port", defaultValue = "80")
	public int port;

	@Parameter(property = "timeout", defaultValue = "120000")
	public int timeout;

	private String getHost() {
		return "http://" + host + ":" + port + "/sheep-dog-dev-svc/";
	}

	private void clearObjects(String goal) throws Exception {
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", tags);
		int retryCount = 0;
		while (retryCount < RETRY_COUNT) {
			try {
				restTemplate.delete(getHost() + "clear" + goal + "Objects?tags={tags}", parameters);
				return; // Exit if successful
			} catch (Exception e) {
				getLog().info("Retry attempt " + (retryCount + 1));
				Thread.sleep(1000);
				retryCount++;
			}
		}
		throw new Exception("Max retries reached while clearing objects for goal: " + goal);
	}

	private String convertObject(String goal, String fileName, String contents) throws Exception {
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", tags);
		parameters.put("fileName", fileName);
		int retryCount = 0;
		while (retryCount < RETRY_COUNT) {
			try {
				return restTemplate
						.postForObject(getHost() + "run" + goal + "?tags={tags}&fileName={fileName}", contents,
								TransformableFile.class, parameters)
						.getFileContent();
			} catch (Exception e) {
				getLog().info("Retry attempt " + (retryCount + 1));
				Thread.sleep(1000);
				retryCount++;
			}
		}
		throw new Exception("Max retries reached while converting objects for goal: " + goal);
	}

	private List<TransformableFile> getObjectNames(String goal) throws Exception {

		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", tags);
		int retryCount = 0;
		while (retryCount < RETRY_COUNT) {
			try {
				ResponseEntity<List<TransformableFile>> response = restTemplate.exchange(
						getHost() + "get" + goal + "ObjectNames?tags={tags}",
						HttpMethod.GET,
						null,
						new ParameterizedTypeReference<List<TransformableFile>>() {
						}, parameters);
				List<TransformableFile> fileList = response.getBody();
				for (TransformableFile tf : fileList) {
					getLog().info("ObjectName: " + tf.getFileName());
				}
				return fileList;
			} catch (Exception e) {
				getLog().info("Retry attempt " + (retryCount + 1));
				Thread.sleep(1000);
				retryCount++;
			}
		}
		throw new Exception("Max retries reached while getting object names for goal: " + goal);
	}

	private void waitForService() throws MojoExecutionException {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < timeout) {
			try {
				ResponseEntity<String> response = restTemplate.getForEntity(
						"http://" + host + ":" + port + "/" + "actuator/health",
						String.class);
				if (response.getStatusCode() == HttpStatus.OK && response.getBody().contains("\"status\":\"UP\"")) {
					getLog().info("Service ready");
					return;
				}
			} catch (Exception e) {
				long timeLeft = (timeout - (System.currentTimeMillis() - startTime)) / 1000;
				getLog().info("Service not ready yet, " + timeLeft + " seconds remaining");
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new MojoExecutionException("Interrupted while waiting for service", e);
			}
		}
		throw new MojoExecutionException("Service did not become available within " + timeout + " milliseconds");
	}

	public void execute(String goal) throws MojoExecutionException {
		execute(goal, "");
	}

	public void execute(String goal, String extension) throws MojoExecutionException {
		getLog().info("Starting execute");
		getLog().info("tags: " + tags);
		getLog().info("baseDir: " + baseDir);

		if (!baseDir.endsWith("/")) {
			baseDir += "/";
		}
		SourceRepository sr = new SourceRepository(baseDir);
		// TODO get the layer 1 directory from the converter
		String[] dirs = { "src/test/resources/asciidoc/", "src-gen/test/resources/cucumber/" };
		try {
			waitForService();
			if (goal.endsWith("ToUML")) {
				clearObjects(goal);
				for (String dir : dirs) {
					ArrayList<String> tempFiles = new ArrayList<String>();
					for (String fileName : sr.list(dir, extension)) {
						if (fileName.startsWith(dir + "stepdefs")) {
							tempFiles.add(fileName);
						} else {
							getLog().info("Converting: " + fileName);
							convertObject(goal, fileName, sr.get(fileName));
						}
					}
					for (String fileName : tempFiles) {
						getLog().info("Converting: " + fileName);
						convertObject(goal, fileName, sr.get(fileName));
					}
				}
			} else {
				for (TransformableFile tf : getObjectNames(goal)) {
					String content = convertObject(goal, tf.getFileName(),
							sr.contains(tf.getFileName()) ? sr.get(tf.getFileName()) : null);
					if (!content.isEmpty()) {
						getLog().info("Converting: " + tf.getFileName());
						sr.put(tf.getFileName(), content);
					}
				}
			}
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new MojoExecutionException(e);
		}
		getLog().info("Ending execute");
	}

}
