package com.pv239.fitin.utils;

import com.pv239.fitin.Entities.Gym;

import java.util.List;

/**
 * Created by xnieslan on 22.05.2016.
 */
public class DataManager {


    /*
    From any class DataManager is accessible by DataManager.getInstance().'anyMethod'();

    Add private attributes that are to be passed among fragments. (Lists, Gyms, Activities..)
    Add getters and setters to obtain or initialized in fragments.
    (Maybe) Add delete that data.
     */

    private static DataManager dataManager = null;

    private DataManager() {
    }

    public static DataManager getInstance() {
        if (dataManager == null)
            dataManager = new DataManager();
        return dataManager;
    }


    /*
    Example in any class.

    List<Gyms> listOfActuallyLoadedGyms = ListDataManager.getInstance().getGyms();
     */
    private List<Gym> gyms = null;

    public List<Gym> getGyms() {
        return gyms;
    }

    public void setGyms(List<Gym> gyms) {
        this.gyms = gyms;
    }
}
