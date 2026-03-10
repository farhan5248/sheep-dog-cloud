package org.farhan.impl;

import java.util.HashMap;

import org.farhan.common.TestObjectGoal;
import org.farhan.objects.maven.UmlToCucumberSpringGoal;
import org.farhan.runners.failsafe.TestConfig;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class UmlToCucumberSpringGoalImpl extends TestObjectGoal implements UmlToCucumberSpringGoal {

	public void transition() {
		runGoal("org.farhan.mbt.maven.UMLToCucumberSpringMojo", TestConfig.getWorkingDir() + "code-prj/");
	}

    @Override
    public void setExecuted(HashMap<String, String> keyMap) {
        // TODO implement later
    }
}
