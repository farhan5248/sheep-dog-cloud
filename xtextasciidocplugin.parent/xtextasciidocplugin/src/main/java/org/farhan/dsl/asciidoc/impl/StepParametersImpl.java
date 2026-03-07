package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.grammar.IDescription;
import org.farhan.dsl.grammar.ILine;
import org.farhan.dsl.grammar.IStepDefinition;
import org.farhan.dsl.grammar.IStepParameters;
import org.farhan.dsl.grammar.ITable;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;

public class StepParametersImpl implements IStepParameters {
    private StepDefinitionImpl parent;
    StepParameters eObject;

    public StepParametersImpl(StepParameters stepParameters) {
        eObject = stepParameters;
    }

    @Override
    public String getName() {
        return eObject.getName();
    }

    @Override
    public IStepDefinition getParent() {
        if (parent == null) {
            parent = new StepDefinitionImpl((StepDefinition) eObject.eContainer());
        }
        return parent;
    }

    @Override
    public IDescription getDescription() {
        if (eObject.getDescription() != null) {
            return new DescriptionImpl(eObject.getDescription());
        }
        return null;
    }

    @Override
    public ITable getTable() {
        return new TableImpl(eObject.getTable());
    }

    @Override
    public void setName(String value) {
        eObject.setName(value);
    }

    @Override
    public void setTable(ITable value) {
        eObject.setTable(((TableImpl) value).eObject);
    }

    @Override
    public void setDescription(IDescription value) {
        eObject.setDescription(((DescriptionImpl) value).eObject);
    }

    @Override
    public boolean addLine(ILine value) {
        org.farhan.dsl.asciidoc.asciiDoc.Description list = eObject.getDescription();
        if (list == null) {
            list = org.farhan.dsl.asciidoc.asciiDoc.AsciiDocFactory.eINSTANCE.createDescription();
            eObject.setDescription(list);
        }
        list.getLineList().add(((LineImpl) value).eObject);
        return true;
    }

}
