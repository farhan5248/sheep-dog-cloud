package org.farhan.dsl.asciidoc.impl;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.farhan.dsl.lang.IResourceRepository;

public class SourceFileRepository implements IResourceRepository {

	private final String projectPath;

	public SourceFileRepository(String uriPath) {
		this.projectPath = uriPath.split("src/test/resources/asciidoc/specs/")[0].replace("/", File.separator);
	}

	@Override
	public void clear(String tags) {
		deleteDir(new File(projectPath));
	}

	@Override
	public boolean contains(String tags, String path) {
		path = projectPath + path.replace("/", File.separator);
		return new File(path).exists();
	}

	@Override
	public void delete(String tags, String path) {
		path = projectPath + path.replace("/", File.separator);
		new File(path).delete();
	}

	private void deleteDir(File aDir) {
		if (aDir.exists()) {
			for (String s : aDir.list()) {
				File f = new File(aDir.getAbsolutePath() + File.separator + s);
				if (f.isDirectory()) {
					deleteDir(f);
				}
				f.delete();
			}
		}
	}

	@Override
	public String get(String tags, String path) throws Exception {
		path = projectPath + path.replace("/", File.separator);
		// TODO make File to Path conversion consistent in Eclipse plugin
		return new String(Files.readAllBytes(new File(path).toPath()), StandardCharsets.UTF_8);
	}

	@Override
	public ArrayList<String> list(String tags, String subDir, String extension) {
		ArrayList<String> files = new ArrayList<String>();
		File aDir = new File(projectPath + subDir.replace("/", File.separator));
		if (aDir.exists()) {
			for (String f : aDir.list()) {
				String aDirObjPath = subDir.replace("/", File.separator) + f;
				if ((new File(projectPath + aDirObjPath)).isDirectory()) {
					files.addAll(list("", aDirObjPath + File.separator, extension));
				} else if (aDirObjPath.toLowerCase().endsWith(extension.toLowerCase())) {
					files.add(aDirObjPath.replace(projectPath, "").replace(File.separator, "/"));
				}
			}
		}
		return files;
	}

	@Override
	public void put(String tags, String path, String content) throws Exception {
		path = projectPath + path.replace("/", File.separator);
		new File(path).getParentFile().mkdirs();
		PrintWriter pw = new PrintWriter(path, StandardCharsets.UTF_8);
		pw.write(content);
		pw.flush();
		pw.close();
	}
}
