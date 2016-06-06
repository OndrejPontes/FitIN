package com.pv239.fitin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    @JsonIgnore
    private String id;
    private String name;
    private String gymName;
    private Coordinates locationCenter;
    private Coordinates southWest;
    private Coordinates northEast;
    private List<String> equipments = new ArrayList<>();
    private List<String> activities = new ArrayList<>();

    public Filter() {
    }

    public Filter(String name) {
        this.name = name;
    }

    //TODO gymName

    public Filter(String name, String gymName, Coordinates locationCenter, Coordinates southWeat, Coordinates northEast, List<String> equipments, List<String> activities) {
        this.name = name;
        this.gymName = gymName;
        this.locationCenter = locationCenter;
        this.southWest = southWeat;
        this.northEast = northEast;
        this.equipments = equipments;
        this.activities = activities;
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

    public String getGymName() {
        return gymName;
    }

    public Coordinates getLocationCenter() {
        return locationCenter;
    }

    public Coordinates getSouthWest() {
        return southWest;
    }

    public Coordinates getNorthEast() {
        return northEast;
    }

    public List<String> getEquipments() {
        return equipments;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public void setLocationCenter(Coordinates locationCenter) {
        this.locationCenter = locationCenter;
    }

    public void setSouthWest(Coordinates southWest) {
        this.southWest = southWest;
    }

    public void setNorthEast(Coordinates northEast) {
        this.northEast = northEast;
    }

    public void setEquipments(List<String> equipments) {
        this.equipments = equipments;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }
}
