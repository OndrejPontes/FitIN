package com.pv239.fitin.dummyData;

import com.pv239.fitin.entities.GymPreview;

import java.util.ArrayList;
import java.util.List;

public class GymPreviewsData {
    private static final String[] ids = {"123", "648", "9865", "675", "687", "6416", "897"};
    private static final String[] names = {"Bull gym", "Ass builder", "Arnold sanctuary", "Teer maker", "Find a girl", "Just own towel", "No scream"};
    private static final int[] ratings = {4, 5, 3, 1, 2 , 7, 5, 6};
    private static final String photoPreviewUrl = "http://lorempixel.com/300/150/sports";
    private static final String[] addresses = {"Brno Botanická 55", "Brno Vranovská 32", "Brno Pisárky 97", "Brno Kohoutovice 32", "Brno Česká 13", "Brno Cejl 0", "Brno Komárov 54"};



    public static List<GymPreview> getListData() {
        List<GymPreview> data = new ArrayList<>();

        //Repeat process 4 times, so that we have enough data to demonstrate a scrollable
        //RecyclerView
        for (int i = 0; i < 14; i++) {
            String id = ids[i % ids.length];
            String name = names[i % names.length];
            int rating = ratings[i % ratings.length];
            String address = addresses[i % addresses.length];
            data.add(new GymPreview(id, name, rating, photoPreviewUrl + "?abcd=" + i, address));
        }
        return data;
    }
}
