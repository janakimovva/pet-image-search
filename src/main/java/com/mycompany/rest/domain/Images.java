package com.mycompany.rest.domain;

import java.util.List;

public class Images {
	String breed;
	List<Image> images;
	public String getBreed() {
		return breed;
	}
	public void setBreed(String breed) {
		this.breed = breed;
	}
	public List<Image> getImages() {
		return images;
	}
	public void setImages(List<Image> images) {
		this.images = images;
	}
}
