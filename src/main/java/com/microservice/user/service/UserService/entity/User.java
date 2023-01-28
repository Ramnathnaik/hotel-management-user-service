package com.microservice.user.service.UserService.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {
	
	@Id
	private String id;
	
	private String userName;
	
	private String email;
	
	private String about;
	
	@Column(length = 10)
	private int phone;
	
	@Transient
	private List<Rating> ratings = new ArrayList<>();
	
}
