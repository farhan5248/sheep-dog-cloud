package org.farhan.objects.specprj.src.test.resources.asciidoc.stepdefs.blahapplication;

import java.util.HashMap;

public interface ObjectPageAsciidocFile {

    public void setContent(HashMap<String, String> keyMap);

    public void assertContent(HashMap<String, String> keyMap);
}
