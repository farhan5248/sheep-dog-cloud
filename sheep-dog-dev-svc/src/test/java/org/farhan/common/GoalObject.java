package org.farhan.common;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.farhan.mbt.model.TransformableFile;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GoalObject extends TestObject {

	private final RestTemplate restTemplate = new RestTemplate();

	public GoalObject() {
		attributes.put("tags", "");
	}

	@Value("${sheepdog.host:dev.sheepdogdev.io}")
	private String serverHost;

	@Value("${sheepdog.port:80}")
	private int serverPort;

	@Value("${sheepdog.timeout:120000}")
	private int timeout;

	private String getHost() {
		return "http://" + serverHost + ":" + serverPort + "/sheep-dog-dev-svc/";
	}

	private void clearObjects(String goal) {
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", attributes.get("tags"));
		restTemplate.delete(getHost() + "clear" + goal + "Objects?tags={tags}", parameters);
	}

	private String convertObject(String goal, String fileName, String contents) {
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", attributes.get("tags"));
		parameters.put("fileName", fileName);
		return restTemplate.postForObject(getHost() + "run" + goal + "?tags={tags}&fileName={fileName}", contents,
				TransformableFile.class, parameters).getFileContent();
	}

	private List<TransformableFile> getObjectNames(String goal) {
		TreeMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("tags", attributes.get("tags"));

		ResponseEntity<List<TransformableFile>> response = restTemplate.exchange(
				getHost() + "get" + goal + "ObjectNames?tags={tags}",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<TransformableFile>>() {
				}, parameters);
		List<TransformableFile> fileList = response.getBody();
		return fileList;
	}

	private void waitForService() throws Exception {
		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < timeout) {
			try {
				ResponseEntity<String> response = restTemplate.getForEntity(
						"http://" + serverHost + ":" + serverPort + "/" + "actuator/health",
						String.class);
				if (response.getStatusCode() == HttpStatus.OK &&
						response.getBody().contains("\"status\":\"UP\"")) {
					return;
				}
			} catch (Exception e) {
				long timeLeft = (timeout - (System.currentTimeMillis() - startTime)) / 1000;
				System.out.println("Service not ready yet, " + timeLeft + " seconds remaining");
			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new Exception("Interrupted while waiting for service", e);
			}
		}

		throw new Exception("Service did not become available within " + timeout + " milliseconds");
	}

	protected void runGoal(String goal) {

		SourceRepository sr = new SourceRepository();
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
							convertObject(goal, fileName, sr.get(fileName));
						}
					}
					for (String fileName : tempFiles) {
						convertObject(goal, fileName, sr.get(fileName));
					}
				}
			} else {
				for (TransformableFile tf : getObjectNames(goal)) {
					String content = convertObject(goal, tf.getFileName(),
							sr.contains(tf.getFileName()) ? sr.get(tf.getFileName()) : null);
					if (!content.isEmpty()) {
						sr.put(tf.getFileName(), content);
					}
				}
			}
		} catch (Exception e) {
			Assertions.fail(getStackTraceAsString(e));
		}
	}
}
