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
}
