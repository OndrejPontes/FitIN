package com.pv239.fitin.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Equipment extends GymStuff {

    @JsonIgnore
    private String id;
    private String name;
    private String description;

    public Equipment() {
    }

    public Equipment(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
