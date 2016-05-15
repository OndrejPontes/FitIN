package com.pv239.fitin.Entities;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private String name;
    private String query;
    private List<Place> locations = new ArrayList<>();
    private List<Equipment> equipments = new ArrayList<>();
    private List<Activity> activities = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Place> getLocations() {
        return locations;
    }

    public void setLocations(List<Place> locations) {
        this.locations = locations;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void addLocation(Place location) {
        this.locations.add(location);
    }

    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }
}
