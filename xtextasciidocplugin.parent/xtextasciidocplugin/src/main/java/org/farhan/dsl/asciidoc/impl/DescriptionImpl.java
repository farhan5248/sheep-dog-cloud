package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;
import java.util.List;

import org.farhan.dsl.lang.IDescription;
import org.farhan.dsl.lang.ILine;
import org.farhan.dsl.asciidoc.asciiDoc.Description;
import org.farhan.dsl.asciidoc.asciiDoc.Line;

public class DescriptionImpl implements IDescription {

	Description eObject;

	public DescriptionImpl(Description description) {
		this.eObject = description;
	}

	@Override
	public ILine getLine(int index) {
		return new LineImpl(eObject.getLineList().get(index));
	}

	@Override
	public ILine getLine(String name) {
		for (Line l : eObject.getLineList()) {
			if (l.getName().equals(name)) {
				return new LineImpl(l);
			}
		}
		return null;
	}

	@Override
	public List<ILine> getLineList() {
		List<ILine> lineList = new ArrayList<ILine>();
		for (Line l : eObject.getLineList()) {
			lineList.add(new LineImpl(l));
		}
		return lineList;
	}

	@Override
	public Object getParent() {
		return eObject.eContainer();
	}

	@Override
	public boolean addLine(ILine value) {
		eObject.getLineList().add(((LineImpl) value).eObject);
		return true;
	}

}
