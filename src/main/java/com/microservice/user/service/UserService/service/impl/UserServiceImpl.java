package com.microservice.user.service.UserService.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.microservice.user.service.UserService.entity.Hotel;
import com.microservice.user.service.UserService.entity.Rating;
import com.microservice.user.service.UserService.entity.User;
import com.microservice.user.service.UserService.exception.ResourceNotFoundException;
import com.microservice.user.service.UserService.external.service.HotelService;
import com.microservice.user.service.UserService.external.service.RatingService;
import com.microservice.user.service.UserService.repository.UserRepository;
import com.microservice.user.service.UserService.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private RatingService ratingService;

	@Override
	public User createUser(User user) {
		// generate unique id
		String randomID = UUID.randomUUID().toString();
		user.setId(randomID);
		return this.userRepository.save(user);
	}

	@Override
	public List<User> getUsers() {
		List<User> users = this.userRepository.findAll();

		users.stream().map(user -> {
			Rating[] ratings = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" + user.getId(),
					Rating[].class);
			List<Rating> ratingList = Arrays.stream(ratings).collect(Collectors.toList());

			List<Rating> newRatingsList = ratingList.stream().map(rating -> {
				Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(),
						Hotel.class);
				rating.setHotel(hotel);
				return rating;
			}).collect(Collectors.toList());
			user.setRatings(newRatingsList);

			return user;
		}).collect(Collectors.toList());

		return users;
	}

	@Override
	public User getUser(String userId) throws ResourceNotFoundException {
		User user = this.userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User with given ID: " + userId + " is not present!"));
		/*
		 * Using Rest Template
		 * 
		 * Rating[] ratings =
		 * restTemplate.getForObject("http://RATING-SERVICE/ratings/users/" +
		 * user.getId(), Rating[].class);
		 * 
		 * List<Rating> ratingsList =
		 * Arrays.stream(ratings).collect(Collectors.toList());
		 * 
		 */

		/*
		 * Using Feign Client
		 */

		List<Rating> ratingsList = ratingService.getRatingsPerUserID(userId);

		List<Rating> newRatingList = ratingsList.stream().map(rating -> {
			/* RestTemplate Approach */
			// Hotel hotel = restTemplate.getForObject("http://HOTEL-SERVICE/hotels/" +
			// rating.getHotelId(), Hotel.class);

			/* Feign Client Appoarch */
			Hotel hotel = hotelService.getHotel(rating.getHotelId());

			rating.setHotel(hotel);
			return rating;
		}).collect(Collectors.toList());

		user.setRatings(newRatingList);

		return user;
	}

	@Override
	public User updateUser(User user) {
		return this.userRepository.save(user);
	}

	@Override
	public void deleteUser(String userId) {
		this.userRepository.deleteById(userId);
	}

}
