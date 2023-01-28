package com.microservice.user.service.UserService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.user.service.UserService.entity.User;

public interface UserRepository extends JpaRepository<User, String>{

}
