package com.pv239.fitin.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Admin on 15.05.2016.
 */
public class Activity {

    @JsonIgnore
    private String id;
    private String name;
    private String description;

    public Activity() {
    }

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
