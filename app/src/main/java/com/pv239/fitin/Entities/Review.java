package com.pv239.fitin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Admin on 15.05.2016.
 */
public class Review {

    @JsonIgnore
    private String id;
    private String userId;
    private String reviewTitle;
    private String reviewText;
    private int rating;
    private String userPhotoUrl;


    public Review() {
    }

    public Review(String userId, String reviewTitle, String reviewText, int rating, String userPhotoUrl) {
        this.userId = userId;
        this.reviewTitle = reviewTitle;
        this.reviewText = reviewText;
        this.rating = rating;
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }
}
