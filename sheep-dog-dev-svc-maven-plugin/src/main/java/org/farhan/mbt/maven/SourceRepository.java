package org.farhan.mbt.maven;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SourceRepository {

	private final String BASEDIR;

	public SourceRepository() {
		BASEDIR = "target/src-gen/";
	}

	public SourceRepository(String baseDir) {
		BASEDIR = baseDir;
	}

	public ArrayList<String> list(String path, String extension) {
		String root = BASEDIR;
		ArrayList<String> files = new ArrayList<String>();
		File aDir = new File(root + path);
		if (aDir.exists()) {
			for (String s : aDir.list()) {
				String aDirObjPath = (path + s);
				File aDirObj = new File(root + aDirObjPath);
				if (aDirObj.isDirectory()) {
					files.addAll(list(aDirObjPath + "/", extension));
				} else if (aDirObjPath.toLowerCase().endsWith(extension.toLowerCase())) {
					files.add(aDirObjPath);
				}
			}
		}
		return files;
	}

	public String get(String path) throws Exception {
		path = BASEDIR + "/" + path;
		return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
	}

	public void put(String path, String content) throws Exception {
		path = BASEDIR + "/" + path;
		new File(path).getParentFile().mkdirs();
		PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8);
		pw.write(content);
		pw.flush();
		pw.close();
	}

	public boolean contains(String path) {
		path = BASEDIR + "/" + path;
		return new File(path).exists();
	}
}
