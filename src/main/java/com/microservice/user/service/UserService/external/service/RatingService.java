package com.microservice.user.service.UserService.external.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservice.user.service.UserService.entity.Rating;

@FeignClient(name = "RATING-SERVICE")
public interface RatingService {
    
    @GetMapping("/ratings/users/{userId}")
    List<Rating> getRatingsPerUserID(@PathVariable String userId);

}
