package com.pv239.fitin.utils;

import android.util.Log;

import com.pv239.fitin.entities.Coordinates;
import com.pv239.fitin.entities.Filter;
import com.pv239.fitin.entities.Gym;
import com.pv239.fitin.entities.GymPreview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Admin on 04.06.2016.
 */
public class GymFiltering {
    private static List<Gym> filteredGym;
    private static Filter filter;

    public static List<GymPreview> filterGymsPreviews(Filter f, List<Gym> gyms) {
        filteredGym = gyms;
        filter = f;

        if(filter == null){
            Log.i(Constants.TAG, "Filer is NULL !!");
            return Collections.emptyList();
        }

        if(filter.getGymName() != null) filterByGymName();
        if(filter.getSouthWest() != null && filter.getNorthEast() != null) filterByLocation();
        if(filter.getEquipments() != null || filter.getEquipments().size() != 0) filterByEquipments();
        if(filter.getActivities() != null || filter.getActivities().size() != 0) filterByActivities();

        List<GymPreview> gymPreviews = new ArrayList<>();
        for(Gym gym : filteredGym) {
            gymPreviews.add(gymToPreview(gym));
        }
        return gymPreviews;
    }

    private static void filterByGymName(){
        String regex = filter.getGymName();

        List<Gym> help = new ArrayList<>(filteredGym);
        for (Gym gym : help) {
            if(!gym.getName().contains(regex)) filteredGym.remove(gym);
        }
    }

    private static void filterByLocation(){
        Coordinates c1 = filter.getNorthEast();
        Coordinates c2 = filter.getSouthWest();
        List<Gym> help = new ArrayList<>(filteredGym);
        for (Gym gym : help) {
            if(gym.getCoordinates().getLatitude() > c1.getLatitude() ||
                    gym.getCoordinates().getLongitude() > c1.getLongitude() ||
                    gym.getCoordinates().getLatitude() < c2.getLatitude() ||
                    gym.getCoordinates().getLongitude() < c2.getLongitude()) filteredGym.remove(gym);
        }
    }

    private static void filterByEquipments(){
        List<String> equipments = filter.getEquipments();
        List<Gym> help = new ArrayList<>(filteredGym);
        boolean remove = true;
        for (Gym gym : help) {
            for (String equipment : equipments) {
                if(gym.getEquipmentList().contains(equipment)) remove = false;
            }
            if(remove) filteredGym.remove(gym);
        }
    }

    private static void filterByActivities(){
        List<String> activities = filter.getActivities();
        List<Gym> help = new ArrayList<>(filteredGym);
        boolean remove = true;
        for (Gym gym : help) {
            for (String activity : activities) {
                if(gym.getActivityList().contains(activity)) remove = false;
            }
            if(remove) filteredGym.remove(gym);
        }
    }

    /**
     * Convert gym to gymPreview
     * @param gym - gym to convert
     * @return converted gymPreview
     */
    public static GymPreview gymToPreview(Gym gym) {
        GymPreview gymPreview = new GymPreview(gym.getId(), gym.getName(), gym.getRating(), gym.getPhotoPreviewUrl(), gym.getAddress());
        return gymPreview;
    }
}
