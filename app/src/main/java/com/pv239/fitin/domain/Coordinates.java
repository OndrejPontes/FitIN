package com.pv239.fitin.domain;

public class Coordinates {

    //x coord
    private float latitude;

    //y coord
    private float longitude;

    public Coordinates() {
    }

    public Coordinates(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates(double latitude, double longitude) {
        this.latitude = (float) latitude;
        this.longitude = (float) longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
