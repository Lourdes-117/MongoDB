package com.lourdes.mongo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
