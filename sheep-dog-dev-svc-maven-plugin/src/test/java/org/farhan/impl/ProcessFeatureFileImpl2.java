package org.farhan.impl;

import java.util.HashMap;

import org.farhan.common.FileObject;
import org.farhan.objects.codeprj.srcgen.test.resources.cucumber.specs.ProcessFeatureFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class ProcessFeatureFileImpl2 extends FileObject implements ProcessFeatureFile {

	@Override
	public void assertContent(HashMap<String, String> keyMap) {
		assertContent(keyMap.get("Content"));
	}

	@Override
	public void setContent(HashMap<String, String> keyMap) {
		setContent(keyMap.get("Content"));
	}
}
