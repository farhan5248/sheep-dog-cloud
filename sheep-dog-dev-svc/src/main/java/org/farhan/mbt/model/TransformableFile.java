package org.farhan.mbt.model;

public class TransformableFile {

	private String fileName;
	private String fileContent;

	public TransformableFile(String fileName, String fileContent) {
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
