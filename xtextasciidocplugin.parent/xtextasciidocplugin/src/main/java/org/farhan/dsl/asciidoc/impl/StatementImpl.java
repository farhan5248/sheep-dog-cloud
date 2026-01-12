package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;

public class StatementImpl implements IStatement {

	Statement eObject;
	private Object parent;

	public StatementImpl(Statement statement) {
		this.eObject = statement;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public void setName(String value) {
		this.eObject.setName(value);
	}

	@Override
	public Object getParent() {
		if (parent == null) {
			if (eObject.eContainer() instanceof org.farhan.dsl.asciidoc.asciiDoc.impl.StepDefinitionImpl) {
				parent = new StepDefinitionImpl((StepDefinition) eObject.eContainer());
			} else if (eObject.eContainer() instanceof org.farhan.dsl.asciidoc.asciiDoc.impl.StepObjectImpl) {
				parent = new StepObjectImpl((StepObject) eObject.eContainer());
			}
		}
		return parent;
	}

}
