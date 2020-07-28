package com.mycompany.rest.resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.rest.domain.Image;
import com.mycompany.rest.domain.ImageList;
import com.mycompany.rest.service.impl.DogServiceImpl;

@RestController
@RequestMapping("/dogs/v1")
public class DogResource {

	@Autowired
	DogServiceImpl service;
	
	// Get All Images or filter images based on user input breed.
	@RequestMapping(value = "/images", method = RequestMethod.GET)
	public ResponseEntity<?> getDogs(@RequestParam(defaultValue ="ALL") String breed) {
		
		ImageList dogList =  service.getDogsServce(breed);
		
		HttpStatus status = null;
		if(dogList.getDogImages().get(0) != null) {	
			status = dogList.getDogImages().get(0).getImages().get(0).getError() == null ? HttpStatus.OK : HttpStatus.NOT_FOUND ;	
		}
		return new ResponseEntity<>(dogList, status);
	}

	// Get Image details based on image id.
	@RequestMapping(value = "/images/{ID}", method = RequestMethod.GET)
	public ResponseEntity<Image> getDogDetail(@PathVariable("ID") String id, HttpServletResponse response) {
		Image dog = new Image();
		try {
		 dog = service.getDogDetailService(Integer.valueOf(id));
		} catch (NumberFormatException e) {
			dog.setError("ID should be Integer.");
			return new ResponseEntity<>(dog,  HttpStatus.BAD_REQUEST);	
		}
		HttpStatus status = dog.getError() == null  ? HttpStatus.OK : HttpStatus.NOT_FOUND ;	
		return new ResponseEntity<>(dog, status);
	}

	// Update votes by user input.
	@RequestMapping(value = "/images/{ID}/vote/{value}", method = RequestMethod.PUT)
	public ResponseEntity<?> setDogRating(@PathVariable("ID") String id, @PathVariable("value") String val, HttpServletRequest request) {
		if(Integer.valueOf(val) != 0 && Integer.valueOf(val) != 1) {
			Image d = new Image();
			d.setError("Rating value should be 1 or 0.");
			return new ResponseEntity<>(d, HttpStatus.BAD_REQUEST);
		}
		int updated = service.updateDogRatingService(Integer.valueOf(id),Integer.valueOf(val),request.getRemoteAddr());
		if(updated == 1) {
			return new ResponseEntity<>("", HttpStatus.OK);
		} else if(updated == 2) {
			Image d = new Image();
			d.setError("You can Vote One Time only, for This Image.");
			return new ResponseEntity<>(d, HttpStatus.BAD_REQUEST);
		}  {
			Image d = new Image();
			d.setError("Failed to Update Rating.");
			return new ResponseEntity<>(d, HttpStatus.BAD_REQUEST);
		}	
	}
}
