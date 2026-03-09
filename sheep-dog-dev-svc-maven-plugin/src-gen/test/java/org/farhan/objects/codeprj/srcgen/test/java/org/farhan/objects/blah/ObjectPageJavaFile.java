package org.farhan.objects.codeprj.srcgen.test.java.org.farhan.objects.blah;

import java.util.HashMap;

public interface ObjectPageJavaFile {

    public void setCreatedAsFollows(HashMap<String, String> keyMap);

    public void setContent(HashMap<String, String> keyMap);

    public void assertCreatedAsFollows(HashMap<String, String> keyMap);

    public void assertContent(HashMap<String, String> keyMap);
}
