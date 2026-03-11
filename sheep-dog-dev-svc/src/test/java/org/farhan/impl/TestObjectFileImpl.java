package org.farhan.impl;

import org.farhan.common.TestObject;
import org.junit.jupiter.api.Assertions;

public abstract class TestObjectFileImpl extends TestObject {

	protected SourceFileRepositoryImpl sr = new SourceFileRepositoryImpl();

	protected String getObjectExists() {
		try {
			boolean exists = sr.contains("", properties.get("object").toString());
			return exists ? "true" : null;
		} catch (Exception e) {
			Assertions.fail(e);
			return null;
		}
	}

	protected void setContent(String docString) {
		try {
			sr.put("", properties.get("object").toString(), docString);
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}

	protected String getContent() {
		try {
			String contents = sr.get("", properties.get("object").toString());
			return contents.replaceAll("\r", "").trim();
		} catch (Exception e) {
			Assertions.fail(e);
			return null;
		}
	}
}
