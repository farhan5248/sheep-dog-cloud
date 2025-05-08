package org.farhan.objects.specprj.src.test.resources.asciidoc.specs.app;

import java.util.HashMap;

public interface ProcessAsciidocFile {

    public void setContent(HashMap<String, String> keyMap);

    public void assertContent(HashMap<String, String> keyMap);
}
