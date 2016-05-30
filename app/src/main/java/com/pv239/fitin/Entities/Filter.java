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
    private List<String> equipments = new ArrayList<>();
    private List<String> activities = new ArrayList<>();

    public Filter() {
    }

    public Filter(String name, String gymName, List<String> equipments, List<String> activities) {
        this.name = name;
        this.gymName = gymName;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public List<String> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<String> equipments) {
        this.equipments = equipments;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }
}
