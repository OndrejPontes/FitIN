package com.pv239.fitin.Entities;

import java.util.List;

public class Gym {
    private String id;
    private String name;
    private String description;
    private int rating;
    private String photoPreviewUrl;
    private String address;
    private Coordinates coordinates;
    private List<String> photosUrls;
    private List<Review> reviews;
    private Boolean isFavourite;
    private List<String> activityList;
    private List<String> equipmentList;

    public Gym() {}

    public Gym(String name, String description, int rating, String photoPreviewUrl, String address, Coordinates coordinates, List<String> photosUrls, List<Review> reviews, Boolean isFavourite, List<String> activityList, List<String> equipmentList) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.photoPreviewUrl = photoPreviewUrl;
        this.address = address;
        this.coordinates = coordinates;
        this.photosUrls = photosUrls;
        this.reviews = reviews;
        this.isFavourite = isFavourite;
        this.activityList = activityList;
        this.equipmentList = equipmentList;
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<String> getPhotosUrls() {
        return photosUrls;
    }

    public void setPhotosUrls(List<String> photosUrls) {
        this.photosUrls = photosUrls;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public List<String> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<String> activityList) {
        this.activityList = activityList;
    }

    public List<String> getEquipmentList() {
        return equipmentList;
    }

    public void setEquipmentList(List<String> equipmentList) {
        this.equipmentList = equipmentList;
    }
}
