package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.grammar.ICell;
import org.farhan.dsl.grammar.IDescription;
import org.farhan.dsl.grammar.ILine;
import org.farhan.dsl.grammar.IResourceRepository;
import org.farhan.dsl.grammar.IRow;
import org.farhan.dsl.grammar.ISheepDogFactory;
import org.farhan.dsl.grammar.IStepDefinition;
import org.farhan.dsl.grammar.IStepObject;
import org.farhan.dsl.grammar.IStepParameters;
import org.farhan.dsl.grammar.ITable;
import org.farhan.dsl.grammar.ITestCase;
import org.farhan.dsl.grammar.ITestData;
import org.farhan.dsl.grammar.ITestProject;
import org.farhan.dsl.grammar.ITestSetup;
import org.farhan.dsl.grammar.ITestStep;
import org.farhan.dsl.grammar.ITestSuite;
import org.farhan.dsl.asciidoc.asciiDoc.AsciiDocFactory;
import org.farhan.dsl.asciidoc.asciiDoc.Cell;
import org.farhan.dsl.asciidoc.asciiDoc.Description;
import org.farhan.dsl.asciidoc.asciiDoc.Row;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;
import org.farhan.dsl.asciidoc.asciiDoc.Text;
import org.farhan.dsl.grammar.IText;

public class AsciiDocFactoryImpl implements ISheepDogFactory {

	private TestProjectImpl testProject;
	private IResourceRepository sr;

	public AsciiDocFactoryImpl(IResourceRepository sourceFileRepository) {
		sr = sourceFileRepository;
	}

	@Override
	public IDescription createDescription() {
		Description description = AsciiDocFactory.eINSTANCE.createDescription();
		return new DescriptionImpl(description);
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

	@Override
	public ITestData createTestData() {
		throw new UnsupportedOperationException("createTestData() is not implemented");
	}

}
