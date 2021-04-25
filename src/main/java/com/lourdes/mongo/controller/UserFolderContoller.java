package com.lourdes.mongo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lourdes.mongo.dao.UserFolderRepository;
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
	
	@GetMapping("/Users/{userName}")
	public ResponseEntity<?> getUserWithUserName(@PathVariable("userName") String userName) {
		List<UserFolderModel> userNameArray = userFolderRepository.findByUserName(userName);
		if (userNameArray.isEmpty()) {
			return new ResponseEntity<>("{}", HttpStatus.OK);
		}
		UserFolderModel userFolder = userNameArray.get(0);
		return new ResponseEntity<>(userFolder, HttpStatus.OK);
	}
}
