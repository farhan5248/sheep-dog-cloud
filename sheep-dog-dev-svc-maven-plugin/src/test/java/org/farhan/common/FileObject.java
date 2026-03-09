package org.farhan.common;

import org.farhan.mbt.maven.SourceFileRepository;
import org.junit.jupiter.api.Assertions;

public abstract class FileObject extends TestObject {

	protected SourceFileRepository sr = new SourceFileRepository("target/src-gen");

	protected void assertObjectExists() {
		try {
			Assertions.assertTrue(sr.contains("", properties.get("path").toString()),
					"The file (" + properties.get("component").toString() + "/" + properties.get("path").toString() + ") isn't present");
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}

	protected void setContent(String docString) {
		try {
			sr.put("", properties.get("component").toString() + "/" + properties.get("path").toString(), docString);
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}

	protected void assertContent(String docString) {
		try {
			String contents = sr.get("", properties.get("component").toString() + "/" + properties.get("path").toString());
			Assertions.assertEquals(docString, contents.replaceAll("\r", "").trim());
		} catch (Exception e) {
			Assertions.fail(e);
		}
	}
}
