package org.farhan.mbt.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
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

	private void clearObjects(String goal) {
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", tags);
		restTemplate.delete(getHost() + "clear" + goal + "Objects?tags={tags}", parameters);
	}

	private String convertObject(String goal, String fileName, String contents) {
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", tags);
		parameters.put("fileName", fileName);
		return restTemplate.postForObject(getHost() + "run" + goal + "?tags={tags}&fileName={fileName}", contents,
				ModelTransformerResponse.class, parameters).content();
	}

	private String[] getObjectNames(String goal) {
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", tags);
		String fileList = restTemplate.getForObject(getHost() + "get" + goal + "ObjectNames?tags={tags}",
				ModelTransformerResponse.class, parameters).fileName();
		if (!fileList.isBlank()) {
			return fileList.split("\n");
		} else {
			return new String[0];
		}
	}

	private void waitForService() throws MojoExecutionException {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < timeout) {
			try {
				ResponseEntity<String> response = restTemplate.getForEntity("http://" + host + ":" + port + "/" + "actuator/health",
						String.class);
				if (response.getStatusCode() == HttpStatus.OK && response.getBody().contains("\"status\":\"UP\"")) {
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
					for (String fileName : sr.list(dir, "")) {
						if (fileName.startsWith(dir + "stepdefs")) {
							tempFiles.add(fileName);
						} else {
							getLog().debug("fileName: " + fileName);
							convertObject(goal, fileName, sr.get(fileName));
						}
					}
					for (String fileName : tempFiles) {
						getLog().debug("fileName: " + fileName);
						convertObject(goal, fileName, sr.get(fileName));
					}
				}
			} else {
				for (String fileName : getObjectNames(goal)) {
					String content = convertObject(goal, fileName, sr.contains(fileName) ? sr.get(fileName) : null);
					if (!content.isEmpty()) {
						sr.put(fileName, content);
					}
				}
			}
		} catch (Exception e) {
			throw new MojoExecutionException(e);
		}
		getLog().info("Ending execute");
	}

}
