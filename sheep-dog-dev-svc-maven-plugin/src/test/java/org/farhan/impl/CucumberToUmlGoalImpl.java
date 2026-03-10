package org.farhan.impl;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.HashMap;

import org.farhan.common.TestObjectGoal;
import org.farhan.objects.maven.CucumberToUmlGoal;
import org.farhan.runners.failsafe.TestConfig;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class CucumberToUmlGoalImpl extends TestObjectGoal implements CucumberToUmlGoal {

	public void setTags(HashMap<String, String> keyMap) {
		properties.put("tags", keyMap.get("Tags"));
	}

	@Override
	public void transition() {
		runGoal("org.farhan.mbt.maven.CucumberToUMLMojo", TestConfig.getWorkingDir() + "code-prj/");
	}

    @Override
    public void setExecutedWith(HashMap<String, String> keyMap) {
        // TODO implement later
    }
}
