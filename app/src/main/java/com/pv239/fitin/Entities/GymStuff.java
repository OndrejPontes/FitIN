package com.pv239.fitin.Entities;

public abstract class GymStuff {

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

    public abstract String getName();

    public abstract String getDescription();
}
