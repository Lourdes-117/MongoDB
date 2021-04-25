package com.lourdes.mongo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		try {
			userFolderRepository.save(userFolderModel);
			return new ResponseEntity<>(userFolderModel, HttpStatus.OK);
		} catch(Exception exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
		String[] filePathArray = filePath.split("/");
		
		Optional<UserFolderModel> userFolderOptional = getUserWithName(userName);
		if (!userFolderOptional.isPresent()) {
			return new ResponseEntity<>("{error: User Not Found}", HttpStatus.NOT_FOUND);
		}
		
		UserFolderModel userFolder = userFolderOptional.get();
		
		//Upload in Home Directory
		if (filePathArray.length == 1) {
			userFolder.setFiles(fileModel);
			try {
				userFolderRepository.save(userFolder);
				return new ResponseEntity<>(userFolder, HttpStatus.OK);
			} catch(Exception exception) {
				return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			for(int index = 0; index < filePathArray.length; index++) {
				System.out.println(index);
				System.out.println(filePathArray[index]);
			}
		}
		
		
		
		return new ResponseEntity<>("Done", HttpStatus.NOT_FOUND);
		
	}
	
	private Optional<UserFolderModel> getUserWithName(String userName) {
		List<UserFolderModel> userNameArray = userFolderRepository.findByUserName(userName);
		if (userNameArray.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(userNameArray.get(0));
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
}
