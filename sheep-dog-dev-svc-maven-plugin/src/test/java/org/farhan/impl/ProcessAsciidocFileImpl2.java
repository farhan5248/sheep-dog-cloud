package org.farhan.impl;

import java.util.HashMap;

import org.farhan.common.FileObject;
import org.farhan.objects.specprj.src.test.resources.asciidoc.specs.ProcessAsciidocFile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class ProcessAsciidocFileImpl2 extends FileObject implements ProcessAsciidocFile {

	@Override
	public void setContent(HashMap<String, String> keyMap) {
		setContent(keyMap.get("Content"));
	}

}
