package org.farhan.impl;

import java.util.HashMap;

import org.farhan.common.TestObjectGoal;
import org.farhan.objects.maven.AsciidoctorToUmlGoal;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class AsciidoctorToUmlGoalImpl extends TestObjectGoal implements AsciidoctorToUmlGoal {

	public void transition() {
		runGoal("asciidoctor", "ConvertAsciidoctorToUML");
	}

	@Override
	public void setTags(HashMap<String, String> keyMap) {
		properties.put("tags", keyMap.get("Tags"));
	}

	@Override
	public void setExecuted(HashMap<String, String> keyMap) {
		// TODO implement later
	}

	@Override
	public void setExecutedWith(HashMap<String, String> keyMap) {
		// TODO implement later
	}
}
