package org.farhan.objects.codeprj.srcgen.test.java.org.farhan.objects.blah;

import java.util.HashMap;

public interface ObjectPageJavaFile {

    public void setContent(HashMap<String, String> keyMap);

    public void assertContent(HashMap<String, String> keyMap);

    public void assertPresent(HashMap<String, String> keyMap);
}
