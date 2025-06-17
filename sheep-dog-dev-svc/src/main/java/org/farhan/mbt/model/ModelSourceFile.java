package org.farhan.mbt.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Model_Source_Files")
public class ModelSourceFile {
    
    @Id
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_content", columnDefinition = "TEXT")
    private String fileContent;
    
    // Default constructor required by JPA
    public ModelSourceFile() {
    }
    
    public ModelSourceFile(String fileName, String fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
    
    // Getters and setters
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileContent() {
        return fileContent;
    }
    
    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
