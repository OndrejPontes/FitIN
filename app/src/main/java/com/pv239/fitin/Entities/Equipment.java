package com.pv239.fitin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Admin on 15.05.2016.
 */
public class Equipment {

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
