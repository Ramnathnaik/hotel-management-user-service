package com.microservice.user.service.UserService.service;

import java.util.List;

import com.microservice.user.service.UserService.entity.User;

public interface UserService {
	
	//create user
	User createUser(User user);
	
	//get all users
	List<User> getUsers();
	
	//get user
	User getUser(String userId);
	
	//update user
	User updateUser(User user);
	
	//delete user
	void deleteUser(String userId);

}
