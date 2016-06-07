package com.pv239.fitin.entities;

public class GymPreview {
    private String id;
    private String name;
    private int rating;
    private String photoPreviewUrl;
    private String address;

    public GymPreview(String id, String name, int rating, String photoPreviewUrl, String address) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.photoPreviewUrl = photoPreviewUrl;
        this.address = address;

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
}
