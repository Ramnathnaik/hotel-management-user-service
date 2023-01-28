package com.microservice.user.service.UserService.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.user.service.UserService.entity.User;
import com.microservice.user.service.UserService.exception.ResourceNotFoundException;
import com.microservice.user.service.UserService.service.impl.UserServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserServiceImpl service;

	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	//Create user
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user) {
		User user2 = this.service.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user2);
	}
	
	//get users
	@GetMapping
	public ResponseEntity<List<User>> getUsers() {
		List<User> users = this.service.getUsers();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
	
	//get user
	@GetMapping("/{userId}")
	// @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
	// @Retry(name = "ratingHotelRetry", fallbackMethod = "ratingHotelFallback")
	@RateLimiter(name = "ratingHotelRateLimiter", fallbackMethod = "ratingHotelFallback")
	public ResponseEntity<User> getUser(@PathVariable String userId) throws ResourceNotFoundException {
		User user = this.service.getUser(userId);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	//update user
	@PutMapping
	public ResponseEntity<User> updateUser(@RequestBody User user) {
		User updatedUser = this.service.updateUser(user);
		return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
	}
	
	//delete user
	@DeleteMapping("/{userId}")
	public void deleteUser(@PathVariable String userId) {
		this.service.deleteUser(userId);
	}

	//Fallback method
	public ResponseEntity<User> ratingHotelFallback(String userId, Exception ex) {
		logger.info("Fallback method is called, as a service is down: " + ex.getMessage());
		User user = User.builder()
		.id("12345")
		.userName("Dummy")
		.email("dummy@email.com")
		.about("A dummy user is created as the service is down")
		.build();
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
}
