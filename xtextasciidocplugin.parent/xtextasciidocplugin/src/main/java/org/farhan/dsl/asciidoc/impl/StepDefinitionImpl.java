package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;
import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.IStepDefinition;
import org.farhan.dsl.lang.IStepObject;
import org.farhan.dsl.lang.IStepParameters;
import org.farhan.dsl.lang.SheepDogUtility;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
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
    public boolean addStatement(IStatement value) {
        eObject.getStatementList().add(((StatementImpl) value).eObject);
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
    public IStatement getStatement(int index) {
        throw new UnsupportedOperationException("getStatement(int index) is not implemented");
    }

    @Override
    public IStatement getStatement(String name) {
        throw new UnsupportedOperationException("getStatement(String name) is not implemented");
    }

    @Override
    public ArrayList<IStatement> getStatementList() {
        ArrayList<IStatement> statementList = new ArrayList<IStatement>();
        for (Statement s : eObject.getStatementList()) {
            statementList.add(new StatementImpl(s));
        }
        return statementList;
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
