package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.lang.INestedDescription;
import org.farhan.dsl.lang.IStepDefinition;
import org.farhan.dsl.lang.IStepParameters;
import org.farhan.dsl.lang.ITable;
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
    public INestedDescription getNestedDescription() {
        if (eObject.getNestedDescription() != null) {
            return new NestedDescriptionImpl(eObject.getNestedDescription());
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
    public void setNestedDescription(INestedDescription value) {
        eObject.setNestedDescription(((NestedDescriptionImpl) value).eObject);
    }

}
