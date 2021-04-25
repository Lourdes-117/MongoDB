package com.lourdes.mongo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.lourdes.mongo.model.UserFolderModel;

@Repository
public interface UserFolderRepository extends MongoRepository<UserFolderModel, String>{
	List<UserFolderModel> findByUserName(String userName);
}
