package com.mycompany.rest.service;

import java.util.List;

import com.mycompany.rest.domain.Image;
import com.mycompany.rest.domain.ImageList;

public interface DogService {
	Image getDogDetailService(Integer id);
	ImageList getDogsServce(String breed);
	int updateDogRatingService(Integer id, Integer value, String ipAddr);
}
