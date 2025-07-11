package org.farhan.common;

import org.farhan.mbt.maven.MBTMojo;
import org.farhan.runners.surefire.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Value;

public abstract class GoalObject extends TestObject {

	@Value("${sheepdog.host:dev.sheepdogdev.io}")
	private String serverHost;

	@Value("${sheepdog.port:80}")
	private int serverPort;

	@Value("${sheepdog.timeout:120000}")
	private int timeout;

	public GoalObject() {
		attributes.put("tags", "");
	}

	protected void runGoal(String goal, String baseDir) {
		try {
			Class<?> mojoClass = Class.forName(goal);
			MBTMojo mojo = (MBTMojo) mojoClass.getConstructor().newInstance();
			mojo.tags = attributes.get("tags");
			mojo.baseDir = baseDir;
			mojo.host = serverHost;
			mojo.port = serverPort;
			mojo.timeout = timeout;
			if (TestConfig.scenarioId != null) {
				mojo.setScenarioId(TestConfig.scenarioId);
			}
			mojo.execute();
		} catch (Exception e) {
			Assertions.fail("There was an error executing the test step\n" + getStackTraceAsString(e));
		}
	}
}
