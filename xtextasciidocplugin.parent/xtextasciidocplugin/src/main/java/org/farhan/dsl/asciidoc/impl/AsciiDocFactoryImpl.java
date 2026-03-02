package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.lang.ICell;
import org.farhan.dsl.lang.IDescription;
import org.farhan.dsl.lang.ILine;
import org.farhan.dsl.lang.INestedDescription;
import org.farhan.dsl.lang.IResourceRepository;
import org.farhan.dsl.lang.IRow;
import org.farhan.dsl.lang.ISheepDogFactory;
import org.farhan.dsl.lang.IStepDefinition;
import org.farhan.dsl.lang.IStepObject;
import org.farhan.dsl.lang.IStepParameters;
import org.farhan.dsl.lang.ITable;
import org.farhan.dsl.lang.ITestCase;
import org.farhan.dsl.lang.ITestProject;
import org.farhan.dsl.lang.ITestSetup;
import org.farhan.dsl.lang.ITestStep;
import org.farhan.dsl.lang.ITestSuite;
import org.farhan.dsl.asciidoc.asciiDoc.AsciiDocFactory;
import org.farhan.dsl.asciidoc.asciiDoc.Cell;
import org.farhan.dsl.asciidoc.asciiDoc.Description;
import org.farhan.dsl.asciidoc.asciiDoc.NestedDescription;
import org.farhan.dsl.asciidoc.asciiDoc.Row;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;
import org.farhan.dsl.asciidoc.asciiDoc.Text;
import org.farhan.dsl.lang.IText;

public class AsciiDocFactoryImpl implements ISheepDogFactory {

	// TODO this assumes that there's just one open project in the eclipse
	// workspace.
	private static TestProjectImpl testProject;
	private static IResourceRepository sr;

	public AsciiDocFactoryImpl(IResourceRepository sourceFileRepository) {
		sr = sourceFileRepository;
	}

	@Override
	public IDescription createDescription() {
		Description description = AsciiDocFactory.eINSTANCE.createDescription();
		return new DescriptionImpl(description);
	}

	@Override
	public INestedDescription createNestedDescription() {
		NestedDescription nestedDescription = AsciiDocFactory.eINSTANCE.createNestedDescription();
		return new NestedDescriptionImpl(nestedDescription);
	}

	@Override
	public IStepDefinition createStepDefinition() {
		StepDefinition stepDefinition = AsciiDocFactory.eINSTANCE.createStepDefinition();
		return new StepDefinitionImpl(stepDefinition);
	}

	@Override
	public IStepObject createStepObject() {
		StepObject eObject = AsciiDocFactory.eINSTANCE.createStepObject();
		return new StepObjectImpl(eObject);
	}

	@Override
	public IStepParameters createStepParameters() {
		StepParameters parameters = AsciiDocFactory.eINSTANCE.createStepParameters();
		return new StepParametersImpl(parameters);
	}

	@Override
	public ITestCase createTestCase() {
		throw new UnsupportedOperationException("createTestCase(String value) is not implemented");
	}

	@Override
	public ITestProject createTestProject() {
		if (testProject == null) {
			testProject = new TestProjectImpl(sr);
		}
		return testProject;
	}

	@Override
	public ITestSetup createTestSetup() {
		throw new UnsupportedOperationException("createTestSetup(String name) is not implemented");
	}

	@Override
	public ITestStep createTestStep() {
		throw new UnsupportedOperationException("createTestStep(String value) is not implemented");
	}

	@Override
	public ITestSuite createTestSuite() {
		throw new UnsupportedOperationException("createTestSuite(String qualifiedName) is not implemented");
	}

	@Override
	public ILine createLine() {
		return new LineImpl(AsciiDocFactory.eINSTANCE.createLine());
	}

	@Override
	public ITable createTable() {
		org.farhan.dsl.asciidoc.asciiDoc.Table table = AsciiDocFactory.eINSTANCE.createTable();
		return new TableImpl(table);
	}

	@Override
	public IRow createRow() {
		Row row = AsciiDocFactory.eINSTANCE.createRow();
		return new RowImpl(row);
	}

	@Override
	public ICell createCell() {
		Cell cell = AsciiDocFactory.eINSTANCE.createCell();
		return new CellImpl(cell);
	}

	@Override
	public IText createText() {
		Text text = AsciiDocFactory.eINSTANCE.createText();
		return new TextImpl(text);
	}

}
