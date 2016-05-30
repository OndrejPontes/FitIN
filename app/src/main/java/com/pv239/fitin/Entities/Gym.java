package com.pv239.fitin.Entities;

import java.util.List;

/**
 * Created by Admin on 15.05.2016.
 */
public class Gym {
    private String id;
    private String name;
    private String description;
    private int rating;
    private String photoPreviewUrl;
    private String address;
    private List<String> photosUrls;
    private List<String> reviews;

    public Gym() {}

    public Gym(String name, String description, int rating, String photoPreviewUrl, String address, List<String> photosUrls, List<String> reviews) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.photoPreviewUrl = photoPreviewUrl;
        this.address = address;
        this.photosUrls = photosUrls;
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPhotoPreviewUrl() {
        return photoPreviewUrl;
    }

    public void setPhotoPreviewUrl(String photoPreviewUrl) {
        this.photoPreviewUrl = photoPreviewUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getPhotosUrls() {
        return photosUrls;
    }

    public void setPhotosUrls(List<String> photosUrls) {
        this.photosUrls = photosUrls;
    }

    public List<String> getReviews() {
        return reviews;
    }

    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }
}
