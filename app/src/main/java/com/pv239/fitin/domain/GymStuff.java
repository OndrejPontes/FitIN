package com.pv239.fitin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class GymStuff {

    @JsonIgnore
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void toggleChecked() {
        isChecked = !isChecked;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getDescription();
}
