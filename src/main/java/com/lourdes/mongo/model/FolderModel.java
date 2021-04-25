package com.lourdes.mongo.model;

import java.util.ArrayList;
import java.util.List;

public class FolderModel {
	String folderName;
	List<FilesModel> files = new ArrayList<FilesModel>();
	List<FolderModel> folders = new ArrayList<FolderModel>();
	
	
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public List<FilesModel> getFiles() {
		return files;
	}
	public void setFiles(List<FilesModel> files) {
		this.files.addAll(files);
	}
	public void setFiles(FilesModel file) {
		this.files.add(file);
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
