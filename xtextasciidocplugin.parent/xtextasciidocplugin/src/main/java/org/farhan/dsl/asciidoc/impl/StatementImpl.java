package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;

public class StatementImpl implements IStatement {

	private Statement statement;

	public StatementImpl(Statement statement) {
		this.statement = statement;
	}

	@Override
	public String getName() {
		return statement.getName();
	}

	@Override
	public void setName(String value) {
		this.statement.setName(value);
	}

}
