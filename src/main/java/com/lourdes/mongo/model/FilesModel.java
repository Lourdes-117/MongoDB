package com.lourdes.mongo.model;

import java.sql.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Files_Model")
public class FilesModel {
	@Id
	private String id;
	private String fileName;
	@CreatedDate
	private Date createdAt;
	@LastModifiedDate
	private Date updatedAt;

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
