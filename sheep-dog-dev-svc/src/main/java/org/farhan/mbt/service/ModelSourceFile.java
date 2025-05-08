package org.farhan.mbt.service;

public class ModelSourceFile {

	private String fileName;
	private String fileContent;

	public ModelSourceFile(String fileName, String fileContent) {
		this.fileName = fileName;
		this.fileContent = fileContent;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileContent() {
		return fileContent;
	}
}
