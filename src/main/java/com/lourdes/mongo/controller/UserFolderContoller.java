package com.lourdes.mongo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lourdes.mongo.dao.UserFolderRepository;
import com.lourdes.mongo.model.FilesModel;
import com.lourdes.mongo.model.FolderModel;
import com.lourdes.mongo.model.UserFolderModel;

@RestController
public class UserFolderContoller {
	@Autowired
	private UserFolderRepository userFolderRepository;
	
	@GetMapping("/Users")
	public ResponseEntity<?> getAllUsers() {
		List<UserFolderModel> allUsers = userFolderRepository.findAll();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}
	
	@GetMapping("/Users/{userName}")
	public ResponseEntity<?> getUserWithUserName(@PathVariable("userName") String userName) {
		Optional<UserFolderModel> userModelOptional = getUserWithName(userName);
		if (!userModelOptional.isPresent()) {
			return new ResponseEntity<>("{}", HttpStatus.OK);
		}
		UserFolderModel userModel = userModelOptional.get();
		return new ResponseEntity<>(userModel, HttpStatus.OK);
	}
	
	@PostMapping("/Users")
	public ResponseEntity<?> newUserCreated(@RequestBody ObjectNode userFolderObjectNode) {
		String userName = userFolderObjectNode.get("userName").asText();
		String firstName = userFolderObjectNode.get("firstName").asText();
		String lastName = userFolderObjectNode.get("lastName").asText();
		String emailID = userFolderObjectNode.get("emailID").asText();
		String phoneNumber = userFolderObjectNode.get("phoneNumber").asText();
		
		if (!(userFolderRepository.findByUserName(userName).isEmpty())) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
		
		UserFolderModel userFolderModel = new UserFolderModel();
		userFolderModel.setUserName(userName);
		userFolderModel.setFirstName(firstName);
		userFolderModel.setLastName(lastName);
		userFolderModel.setEmailID(emailID);
		userFolderModel.setPhoneNumber(phoneNumber);
		return saveToRepository(userFolderModel);
	}
	
	@PostMapping("/addFile")
	public ResponseEntity<?> addFileToUser(@RequestBody ObjectNode addFileObjectNode) {
		String userName = addFileObjectNode.get("userName").asText();
		String fileName = addFileObjectNode.get("fileName").asText();
		String fileType = addFileObjectNode.get("fileType").asText();
		String fileUrl = addFileObjectNode.get("fileUrl").asText();
		String filePath = addFileObjectNode.get("filePath").asText();
		FilesModel fileModel = new FilesModel();
		fileModel.setFileName(fileName);
		fileModel.setFileType(fileType);
		fileModel.setFileUrl(fileUrl);
		fileModel.setId(createIdentifier(filePath, fileName));
		String[] filePathArray = filePath.split("/");
		
		Optional<UserFolderModel> userFolderOptional = getUserWithName(userName);
		if (!userFolderOptional.isPresent()) {
			return new ResponseEntity<>("{error: User Not Found}", HttpStatus.NOT_FOUND);
		}
		
		UserFolderModel userFolder = userFolderOptional.get();
		
		//Upload in Home Directory
		if (filePathArray.length == 1) {
			if(doesFileExist(fileName, userFolder.getFiles())) {
				return new ResponseEntity<>("{error: File Already Exists}", HttpStatus.CONFLICT);
			}
			userFolder.setFiles(fileModel);
			return saveToRepository(userFolder);
		} else {
			//Upload To Inner Directory
			Optional<FolderModel> folderToSaveAt = navigatoToPath(filePathArray, userFolder.getFolders());
			if(!folderToSaveAt.isPresent()) {
				return new ResponseEntity<>("{error: Folder Path Does Not Exist}", HttpStatus.NOT_FOUND);
			}
			FolderModel folderToSaveAtFound = folderToSaveAt.get();
			if(doesFileExist(fileName, folderToSaveAtFound.getFiles())) {
				return new ResponseEntity<>("{error: File Already Exists}", HttpStatus.CONFLICT);
			}
			folderToSaveAtFound.setFiles(fileModel);
			return saveToRepository(userFolder);
		}
	}
	
	@PostMapping("/addFolder")
	public ResponseEntity<?> addFolderToUser(@RequestBody ObjectNode addFileObjectNode) {
		String userName = addFileObjectNode.get("userName").asText();
		String folderName = addFileObjectNode.get("folderName").asText();
		String folderPath = addFileObjectNode.get("folderPath").asText();
		
		FolderModel folderModel = new FolderModel();
		folderModel.setFolderName(folderName);
		folderModel.setId(createIdentifier(folderPath, folderName));
		
		String[] filePathArray = folderPath.split("/");
		
		Optional<UserFolderModel> userFolderOptional = getUserWithName(userName);
		if (!userFolderOptional.isPresent()) {
			return new ResponseEntity<>("{error: User Not Found}", HttpStatus.NOT_FOUND);
		}
		
		UserFolderModel userFolder = userFolderOptional.get();
		
		//Upload in Home Directory
		if (filePathArray.length == 1) {
			if(doesFolderExist(folderName, userFolder.getFolders())) {
				return new ResponseEntity<>("{error: Folder Already Exists}", HttpStatus.CONFLICT);
			}
			userFolder.setFolders(folderModel);
			return saveToRepository(userFolder);
		} else {
			//Upload In Inner Directory
			Optional<FolderModel> folderToSaveAt = navigatoToPath(filePathArray, userFolder.getFolders());
			if(!folderToSaveAt.isPresent()) {
				return new ResponseEntity<>("{error: Folder Path Does Not Exist}", HttpStatus.NOT_FOUND);
			}
			FolderModel folderToSaveAtFound = folderToSaveAt.get();
			if(doesFolderExist(folderName, folderToSaveAtFound.getFolders())) {
				return new ResponseEntity<>("{error: Folder Already Exists}", HttpStatus.CONFLICT);
			}
			folderToSaveAtFound.setFolders(folderModel);
			return saveToRepository(userFolder);
		}
	}
	
	@DeleteMapping("/deleteFolder")
	public ResponseEntity<?> deleteFolder(@RequestBody ObjectNode deleteFolderObjectNode) {
		String userName = deleteFolderObjectNode.get("userName").asText();
		String folderName = deleteFolderObjectNode.get("folderName").asText();
		String folderPath = deleteFolderObjectNode.get("folderPath").asText();
		String[] filePathArray = createIdentifier(folderPath, folderName).split("/");

		Optional<UserFolderModel> userFolderOptional = getUserWithName(userName);
		if (!userFolderOptional.isPresent()) {
			return new ResponseEntity<>("{error: User Not Found}", HttpStatus.NOT_FOUND);
		}
		
		UserFolderModel userFolder = userFolderOptional.get();
		userFolder.deleteFolder(filePathArray);
		return saveToRepository(userFolder);
	}
	
	@DeleteMapping("/deleteFile")
	public ResponseEntity<?> deleteFile(@RequestBody ObjectNode deleteFolderObjectNode) {
		String userName = deleteFolderObjectNode.get("userName").asText();
		String fileName = deleteFolderObjectNode.get("fileName").asText();
		String filePath = deleteFolderObjectNode.get("filePath").asText();
		String[] filePathArray = createIdentifier(filePath, fileName).split("/");

		Optional<UserFolderModel> userFolderOptional = getUserWithName(userName);
		if (!userFolderOptional.isPresent()) {
			return new ResponseEntity<>("{error: User Not Found}", HttpStatus.NOT_FOUND);
		}
		
		UserFolderModel userFolder = userFolderOptional.get();
		userFolder.deleteFile(filePathArray);
		return saveToRepository(userFolder);
	}
	
	private String createIdentifier(String path, String fileOrFolderName) {
		if ((path.charAt(path.length()-1)) == '/') {
			path = path.concat(fileOrFolderName);
		} else {
			path = path.concat("/");
			path = path.concat(fileOrFolderName);
		}
		return path;
	}
	
	private Optional<FolderModel> navigatoToPath(String[] filePathArray, List<FolderModel> homeFolder) {
		List<FolderModel> folderList = homeFolder;
		Optional<FolderModel> folderToSaveAt= Optional.empty();
		for(int index = 0; index < filePathArray.length-1; index++) {
			folderToSaveAt = getFolderWithName(filePathArray[index+1], folderList);
			if(folderToSaveAt.isPresent()) {
				folderList = folderToSaveAt.get().getFolders();
			}
		}
		return folderToSaveAt;
	}

	private ResponseEntity<?> saveToRepository(UserFolderModel userFolder) {
		try {
			userFolderRepository.save(userFolder);
			return new ResponseEntity<>(userFolder, HttpStatus.OK);
		} catch(Exception exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private Optional<FolderModel> getFolderWithName(String folderName, List<FolderModel> folders) {
		for(int iteration = 0; iteration < folders.size(); iteration++) {
			if (folderName.equals(folders.get(iteration).getFolderName())) {
				return Optional.of(folders.get(iteration));
			}
		}
		return Optional.empty();
	}
	
	private Optional<UserFolderModel> getUserWithName(String userName) {
		List<UserFolderModel> userNameArray = userFolderRepository.findByUserName(userName);
		if (userNameArray.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(userNameArray.get(0));
	}
	
	private boolean doesFolderExist(String folderName, List<FolderModel> folders) {
		for (FolderModel folder : folders) {
			if(folder.getFolderName().equals(folderName)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean doesFileExist(String fileName, List<FilesModel> files) {
		for (FilesModel file : files) {
			if(file.getFileName().equals(fileName)) {
				return true;
			}
		}
		return false;
	}
}
