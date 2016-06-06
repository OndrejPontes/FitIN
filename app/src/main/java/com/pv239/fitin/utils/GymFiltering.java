package com.pv239.fitin.utils;

import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.Entities.Gym;
import com.pv239.fitin.Entities.GymPreview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 04.06.2016.
 */
public class GymFiltering {
    public static List<GymPreview> filterGymsPreviews(Filter filter, List<Gym> gyms) {
        // Todo: some cool filtering stuff...
        List<Gym> filteredGym = gyms;
        List<GymPreview> gymPreviews = new ArrayList<>();
        for(Gym gym : filteredGym) {
            gymPreviews.add(gymToPreview(gym));
        }
        return gymPreviews;
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
