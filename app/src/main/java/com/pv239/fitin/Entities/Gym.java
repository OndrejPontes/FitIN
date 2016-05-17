package com.pv239.fitin.Entities;

import java.util.List;

/**
 * Created by Admin on 15.05.2016.
 */
public class Gym {
    private int id;
    private String name;
    private String description;
    private String rating;
    private String photoPreviewUrl;
    private String address;
    private List<String> photosUrls;
    private List<Review> reviews;

    public Gym() {}

    public Gym(int id, String name, String rating, String photoPreviewUrl, String address) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.photoPreviewUrl = photoPreviewUrl;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    public String getPhotoPreviewUrl() {
        return photoPreviewUrl;
    }

    public String getAddress() {
        return address;
    }

    public List<String> getPhotosUrls() {
        return photosUrls;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setPhotoPreviewUrl(String photoPreviewUrl) {
        this.photoPreviewUrl = photoPreviewUrl;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhotosUrls(List<String> photosUrls) {
        this.photosUrls = photosUrls;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
