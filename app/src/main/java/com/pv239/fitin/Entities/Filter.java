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
    private Coordinates coordinates;
    private List<String> equipments = new ArrayList<>();
    private List<String> activities = new ArrayList<>();

    public Filter() {
    }

    public Filter(String name) {
        this.name = name;
    }

    //TODO gymName
    public Filter(String name, String gymName, Coordinates coordinates, List<String> equipments, List<String> activities) {
        this.name = name;
        this.gymName = gymName;
        this.coordinates = coordinates;
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

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public List<String> getEquipments() {
        return equipments;
    }

    public List<String> getActivities() {
        return activities;
    }
}
