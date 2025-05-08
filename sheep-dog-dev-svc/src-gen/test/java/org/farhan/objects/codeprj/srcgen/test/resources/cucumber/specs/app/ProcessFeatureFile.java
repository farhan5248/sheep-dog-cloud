package org.farhan.objects.codeprj.srcgen.test.resources.cucumber.specs.app;

import java.util.HashMap;

public interface ProcessFeatureFile {

    public void setContent(HashMap<String, String> keyMap);

    public void assertContent(HashMap<String, String> keyMap);
}
