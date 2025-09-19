package org.farhan.mbt.service.cucumber;

import java.util.ArrayList;

import org.farhan.dsl.cucumber.CucumberStandaloneSetup;
import org.farhan.mbt.core.IConvertibleObject;
import org.farhan.mbt.core.ConvertibleProject;
import org.farhan.dsl.lang.IResourceRepository;

public class CucumberTestProject extends ConvertibleProject {

	public CucumberTestProject(String tags, IResourceRepository fa) {
		super(tags, fa);
	}

	@Override
	public IConvertibleObject addFile(String path) throws Exception {
		// TODO calculate an actual checksum
		fa.put(tags, path, "sha checksum");
		IConvertibleObject aIConvertibleObject = getObject(path);
		if (aIConvertibleObject != null) {
			return aIConvertibleObject;
		} else {

			if (!path.startsWith(getDir(TEST_CASES))) {
				if (path.startsWith(getDir(TEST_STEPS))) {
					aIConvertibleObject = createClass(path);
					secondLayerObjects.add(aIConvertibleObject);
				} else {
					aIConvertibleObject = new CucumberInterface(path);
					thirdLayerObjects.add(aIConvertibleObject);
				}
			} else {
				aIConvertibleObject = new CucumberFeature(path);
				firstLayerObjects.add(aIConvertibleObject);
			}
			return aIConvertibleObject;
		}
	}

	protected IConvertibleObject createClass(String path) {
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
	public ArrayList<IConvertibleObject> getObjects(String layer) {

		ArrayList<IConvertibleObject> layerObjects = null;
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
	public void deleteObject(IConvertibleObject srcObj) {
		firstLayerObjects.remove(srcObj);
		secondLayerObjects.remove(srcObj);
		thirdLayerObjects.remove(srcObj);
	}

}
