package com.pv239.fitin.Entities;

public class Coordinates {

    //x coord
    private float latitude;

    //y coord
    private float longitude;

    public Coordinates(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }
}
