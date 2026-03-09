package org.farhan.impl;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.HashMap;

import org.farhan.common.GoalObject;
import org.farhan.objects.maven.UmlToAsciidoctorGoal;
import org.farhan.runners.failsafe.TestConfig;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class UmlToAsciidoctorGoalImpl extends GoalObject implements UmlToAsciidoctorGoal {

	public void transition() {
		runGoal("org.farhan.mbt.maven.UMLToAsciiDoctorMojo", TestConfig.getWorkingDir() + "spec-prj/");
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
