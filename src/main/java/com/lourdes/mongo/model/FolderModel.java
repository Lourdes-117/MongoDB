package com.lourdes.mongo.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Folders_Model")
public class FolderModel {
	@Id
	String id;
	String folderName;
	List<FilesModel> files = new ArrayList<FilesModel>();
	List<FolderModel> folders = new ArrayList<FolderModel>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	
	public boolean deleteFolder(LinkedList<String> folderPathList) {
		int iteration = 0;
		boolean isFound = false;
		for(; iteration < getFolders().size(); iteration++) {
			if(getFolders().get(iteration).folderName.equals(folderPathList.get(1))) {
				isFound = true;
				break;
			}
		}
		if(isFound) {
			if(folderPathList.size() > 2) {
				folderPathList.remove(0);
				return getFolders().get(iteration).deleteFolder(folderPathList);
			} else {
				getFolders().remove(iteration);
				return true;
			}
		}
		return isFound;
	}
}
