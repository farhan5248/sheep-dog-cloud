package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;
import org.farhan.dsl.lang.IDescription;
import org.farhan.dsl.lang.ILine;
import org.farhan.dsl.lang.IStepDefinition;
import org.farhan.dsl.lang.IStepObject;
import org.farhan.dsl.lang.IStepParameters;
import org.farhan.dsl.lang.SheepDogUtility;
import org.farhan.dsl.asciidoc.asciiDoc.AsciiDocFactory;
import org.farhan.dsl.asciidoc.asciiDoc.Description;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;

public class StepDefinitionImpl implements IStepDefinition {

    private StepObjectImpl parent;
    StepDefinition eObject;

    public StepDefinitionImpl(StepDefinition value) {
        this.eObject = value;
    }

    @Override
    public boolean addLine(ILine value) {
        Description list = eObject.getDescription();
        if (list == null) {
            list = AsciiDocFactory.eINSTANCE.createDescription();
            eObject.setDescription(list);
        }
        list.getLineList().add(((LineImpl) value).eObject);
        return true;
    }

    @Override
    public boolean addStepParameters(IStepParameters value) {

        EList<StepParameters> unsortedList = eObject.getStepParameterList();
        TreeMap<String, StepParameters> sortedMap = new TreeMap<String, StepParameters>();
        StepParameters aStepParameter = ((StepParametersImpl) value).eObject;
        sortedMap.put(aStepParameter.getName(), aStepParameter);
        for (StepParameters sp : unsortedList) {
            sortedMap.put(sp.getName(), sp);
        }
        unsortedList.clear();
        for (String key : sortedMap.keySet()) {
            unsortedList.add(sortedMap.get(key));
        }
        return true;
    }

    @Override
    public String getName() {
        return eObject.getName();
    }

    @Override
    public IStepObject getParent() {
        if (parent == null) {
            parent = new StepObjectImpl((StepObject) eObject.eContainer());
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
    public ArrayList<IStepParameters> getStepParameterList() {
        ArrayList<IStepParameters> list = new ArrayList<IStepParameters>();
        for (StepParameters t : eObject.getStepParameterList()) {
            StepParametersImpl stepParameters = new StepParametersImpl((StepParameters) t);
            list.add(stepParameters);
        }
        return list;
    }

    @Override
    public IStepParameters getStepParameters(int index) {
        throw new UnsupportedOperationException("getStepParameters(int index) is not implemented");
    }

    @Override
    public IStepParameters getStepParameters(String name) {
        for (IStepParameters sp : getStepParameterList()) {
            String rowAsString = SheepDogUtility
                    .getCellListAsString(sp.getTable().getRowList().getFirst().getCellList());
            if (name.contentEquals(rowAsString)) {
                return sp;
            }
        }
        return null;
    }

    @Override
    public void setName(String value) {
        eObject.setName(value);
    }

}
