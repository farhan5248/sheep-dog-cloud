package org.farhan.common;

import org.farhan.mbt.maven.SourceRepository;
import org.junit.jupiter.api.Assertions;

public abstract class FileObject extends TestObject {

	protected SourceRepository sr = new SourceRepository("target/src-gen");

	protected void assertObjectExists() {
		try {
			Assertions.assertTrue(sr.contains(attributes.get("path")),
					"The file (" + attributes.get("component") + "/" + attributes.get("path") + ") isn't present");
		} catch (Exception e) {
			Assertions.fail(getStackTraceAsString(e));
		}
	}

	protected void setContent(String docString) {
		try {
			sr.put(attributes.get("component") + "/" + attributes.get("path"), docString);
		} catch (Exception e) {
			Assertions.fail(getStackTraceAsString(e));
		}
	}

	protected void assertContent(String docString) {
		try {
			String contents = sr.get(attributes.get("component") + "/" + attributes.get("path"));
			Assertions.assertEquals(docString, contents.replaceAll("\r", "").trim());
		} catch (Exception e) {
			Assertions.fail(getStackTraceAsString(e));
		}
	}
}
