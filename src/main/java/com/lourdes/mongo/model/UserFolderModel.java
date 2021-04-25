package com.lourdes.mongo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User_Folders")
public class UserFolderModel {
	@Id
	private String id;
	private String userName;
	private String firstName;
	private String lastName;
	private String emailID;
	private String phoneNumber;
	private List<FilesModel> files = new ArrayList<FilesModel>();
	private List<FolderModel> folders = new ArrayList<FolderModel>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public List<FilesModel> getFiles() {
		return files;
	}
	public void setFiles(FilesModel file) {
		this.files.add(file);
	}
	
	public void setFiles(List<FilesModel> files) {		
		this.files.addAll(files);
	}
	
	public List<FolderModel> getFolders() {
		return folders;
	}
	public void setFolders(List<FolderModel> folders) {
		this.folders.addAll(folders);
	}
	public void setFolders(FolderModel folder) {
		this.folders.add(folder);
	}
}
