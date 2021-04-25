package com.lourdes.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User_Folders")
public class UserFolderModel {
	@Id
	private String id;
	private String userName;
	private FilesModel files;
}
