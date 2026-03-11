package org.farhan.impl;

import org.farhan.common.TestObject;
import org.farhan.mbt.maven.SourceFileRepository;
import org.junit.jupiter.api.Assertions;

public abstract class TestObjectFileImpl extends TestObject {

	protected SourceFileRepository sr = new SourceFileRepository("target/src-gen");

	protected String getObjectExists() {
		try {
			boolean exists = sr.contains("", properties.get("component").toString() + "/" + properties.get("object").toString());
			return exists ? "true" : null;
		} catch (Exception e) {
			Assertions.fail(e);
			return null;
		}
	}

	protected void setContent(String docString) {
		try {
			sr.put("", properties.get("component").toString() + "/" + properties.get("object").toString(), docString);
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}

	protected String getContent() {
		try {
			String contents = sr.get("", properties.get("component").toString() + "/" + properties.get("object").toString());
			return contents.replaceAll("\r", "").trim();
		} catch (Exception e) {
			Assertions.fail(e);
			return null;
		}
	}
}
