package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.lang.ITestStep;
import org.farhan.dsl.lang.IText;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;
import org.farhan.dsl.asciidoc.asciiDoc.Text;

public class TextImpl implements IText {

	private TestStepImpl parent;
	Text eObject;

	public TextImpl(Text text) {
		eObject = text;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public ITestStep getParent() {
		if (parent == null) {
			parent = new TestStepImpl((TestStep) eObject.eContainer());
		}
		return parent;
	}

}
