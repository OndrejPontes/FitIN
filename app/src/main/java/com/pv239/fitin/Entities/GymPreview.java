package com.pv239.fitin.Entities;

import android.location.Address;

/**
 * Created by Admin on 15.05.2016.
 */
public class GymPreview {
    private int id;
    private String name;
    private String rating;
    private String photoPreviewUrl;
    private String address;

    public GymPreview(int id, String name, String rating, String photoPreviewUrl, String address) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.photoPreviewUrl = photoPreviewUrl;
        this.address = address;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
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
