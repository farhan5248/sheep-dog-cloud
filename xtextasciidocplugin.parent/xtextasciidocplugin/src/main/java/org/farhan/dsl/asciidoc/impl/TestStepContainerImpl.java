package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.ITestStep;
import org.farhan.dsl.lang.ITestStepContainer;
import org.farhan.dsl.lang.ITestSuite;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;
import org.farhan.dsl.asciidoc.asciiDoc.TestStepContainer;
import org.farhan.dsl.asciidoc.asciiDoc.TestSuite;

public class TestStepContainerImpl implements ITestStepContainer {

    TestStepContainer eObject;
    private TestSuiteImpl parent;

    public TestStepContainerImpl(TestStepContainer testCase) {
        this.eObject = testCase;
    }

    @Override
    public String getName() {
        return eObject.getName();
    }

    @Override
    public ITestSuite getParent() {
        if (parent == null) {
            parent = new TestSuiteImpl((TestSuite) eObject.eContainer());
        }
        return parent;
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
    public ArrayList<ITestStep> getTestStepList() {
        ArrayList<ITestStep> testStepList = new ArrayList<ITestStep>();
        for (TestStep s : eObject.getTestStepList()) {
            TestStepImpl testStep = new TestStepImpl(s);
            testStepList.add(testStep);
        }
        return testStepList;
    }

    @Override
    public void setName(String value) {
        throw new UnsupportedOperationException("setName(String value) is not implemented");
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
    public ITestStep getTestStep(int index) {
        throw new UnsupportedOperationException("getTestStep(int index) is not implemented");
    }

    @Override
    public ITestStep getTestStep(String name) {
        throw new UnsupportedOperationException("getTestStep(String name) is not implemented");
    }

    @Override
    public boolean addStatement(IStatement value) {
        eObject.getStatementList().add(((StatementImpl) value).eObject);
        return true;
    }

    @Override
    public boolean addTestStep(ITestStep value) {
        eObject.getTestStepList().add(((TestStepImpl) value).eObject);
        return true;
    }

}
