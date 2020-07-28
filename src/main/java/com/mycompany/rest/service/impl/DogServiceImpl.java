package com.mycompany.rest.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.rest.Repository.DogJdbcRepository;
import com.mycompany.rest.domain.Image;
import com.mycompany.rest.domain.ImageList;
import com.mycompany.rest.domain.Images;
import com.mycompany.rest.service.DogService;

@Component
public class DogServiceImpl implements DogService {

	@Autowired
	private DogJdbcRepository repository;

	@Override
	public ImageList getDogsServce(String breed) {
		ImageList dogsImageList = new ImageList();
		List<Images> imagesDetails = new ArrayList<>();
		//Returns all dog images group by breed.
		if (breed.equalsIgnoreCase("ALL")) {
			//Getting all Image details.
			Map<String, List<Image>> lis = repository.findAll().stream().collect(Collectors.groupingBy(Image::getBreed));
			//Grouping by breed
			lis.entrySet().forEach(dm -> {
				List<Image> dogsSOrt = dm.getValue();
				dogsSOrt.sort(Comparator.comparing(Image::getVotes).reversed());
				Images imagesDetail = new Images();
				imagesDetail.setBreed(dm.getKey());
				imagesDetail.setImages(dogsSOrt);
				imagesDetails.add(imagesDetail);
			});
			dogsImageList.setDogImages(imagesDetails);
		} else {
			//Getting Image details by breed.
			List<Image> dogsbyBreed = repository.findAll().stream().filter(d -> d.getBreed().equalsIgnoreCase(breed))
					.collect(Collectors.toList());
			Images imagesDetail = new Images();
			dogsbyBreed.sort(Comparator.comparing(Image::getVotes).reversed());
			if (dogsbyBreed != null) {
				Image d = new Image();
				d.setBreed(breed);
				d.setError("No results found.");
				dogsbyBreed.add(d);

			}
			imagesDetail.setBreed(breed);
			imagesDetail.setImages(dogsbyBreed);
			imagesDetails.add(imagesDetail);
			dogsImageList.setDogImages(imagesDetails);
		}

		return dogsImageList;
	}

	@Override
	public Image getDogDetailService(Integer id) {
		//Get Image details by Image Id
		if(repository.findById(id).size() != 0) {
		return repository.findById(id).get(0);
		} else {
			Image d = new Image();
			d.setError("Results not fount.");;
			return d;
		}
	}
			
	@Override
	public int updateDogRatingService(Integer id, Integer value,String ipAddr) {
		int tmp;
		//setting vote up or down based on user decession.
		if(value == 0) {
			tmp = -1;
		} else {
			tmp = 1;
		}
		//Updating vote for image by image ID.
		return repository.updateRatingById(id,tmp,ipAddr);
	}
};