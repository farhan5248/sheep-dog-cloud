package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.ITestCase;
import org.farhan.dsl.lang.ITestData;
import org.farhan.dsl.lang.ITestStep;
import org.farhan.dsl.asciidoc.asciiDoc.TestCase;

public class TestCaseImpl extends TestStepContainerImpl implements ITestCase {

    public TestCaseImpl(TestCase testCase) {
        super(testCase);
    }

    @Override
    public ArrayList<ITestData> getTestDataList() {
        throw new UnsupportedOperationException("ArrayList<ITestData> getTestDataList() is not implemented");
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
    public ITestData getTestData(int index) {
        throw new UnsupportedOperationException("getTestData(int index) is not implemented");
    }

    @Override
    public ITestData getTestData(String name) {
        throw new UnsupportedOperationException("getTestData(String name) is not implemented");
    }

}
