package org.farhan.mbt.next.cucumber;

import java.util.ArrayList;

import org.farhan.dsl.cucumber.CucumberStandaloneSetup;
import org.farhan.mbt.core.ConvertibleObject;
import org.farhan.mbt.core.ConvertibleProject;
import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.cucumber.CucumberClass;
import org.farhan.mbt.cucumber.CucumberFeature;
import org.farhan.mbt.cucumber.CucumberInterface;

public class CucumberTestProject extends ConvertibleProject {

	public CucumberTestProject(String tags, ObjectRepository fa) {
		super(tags, fa);
	}

	@Override
	public ConvertibleObject addFile(String path) throws Exception {
		// TODO calculate an actual checksum
		fa.put(tags, path, "sha checksum");
		ConvertibleObject aConvertibleObject = getObject(path);
		if (aConvertibleObject != null) {
			return aConvertibleObject;
		} else {

			if (!path.startsWith(getDir(TEST_CASES))) {
				if (path.startsWith(getDir(TEST_STEPS))) {
					aConvertibleObject = createClass(path);
					secondLayerObjects.add(aConvertibleObject);
				} else {
					aConvertibleObject = new CucumberInterface(path);
					thirdLayerObjects.add(aConvertibleObject);
				}
			} else {
				aConvertibleObject = new CucumberFeature(path);
				firstLayerObjects.add(aConvertibleObject);
			}
			return aConvertibleObject;
		}
	}

	protected ConvertibleObject createClass(String path) {
		return new CucumberClass(path);
	}

	@Override
	public String getDir(String layer) {
		switch (layer) {
		case TEST_CASES:
			return "src-gen/test/resources/cucumber/" + TEST_CASES;
		case TEST_STEPS:
			return "src-gen/test/java/org/farhan/" + TEST_STEPS;
		case TEST_OBJECTS:
			return "src-gen/test/java/org/farhan/" + TEST_OBJECTS;
		default:
			return "";
		}
	}

	@Override
	public String getFileExt(String layer) {
		if (layer.contentEquals(TEST_CASES)) {
			return ".feature";
		} else {
			return ".java";
		}
	}

	@Override
	public ArrayList<ConvertibleObject> getObjects(String layer) {

		ArrayList<ConvertibleObject> layerObjects = null;
		switch (layer) {
		case TEST_CASES:
			layerObjects = firstLayerObjects;
			break;
		case TEST_STEPS:
			layerObjects = secondLayerObjects;
			break;
		case TEST_OBJECTS:
			layerObjects = thirdLayerObjects;
			break;
		}
		return layerObjects;
	}

	@Override
	public void init() throws Exception {
		CucumberStandaloneSetup.doSetup();
	}

	@Override
	public void deleteObject(ConvertibleObject srcObj) {
		firstLayerObjects.remove(srcObj);
		secondLayerObjects.remove(srcObj);
		thirdLayerObjects.remove(srcObj);
	}

}
