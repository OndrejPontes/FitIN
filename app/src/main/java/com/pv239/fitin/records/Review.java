package com.pv239.fitin.records;

import com.orm.SugarRecord;

/**
 * Created by opontes on 11/05/16.
 */
public class Review extends SugarRecord {
    String name;
    String description;

    public Review() {
    }

    public Review(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Review{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
