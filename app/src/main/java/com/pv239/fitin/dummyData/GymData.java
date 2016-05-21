package com.pv239.fitin.dummyData;

import android.util.Log;

import com.pv239.fitin.Entities.Gym;
import com.pv239.fitin.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 16.05.2016.
 */
public class GymData {
    private static final String[] names = {"Bull gym", "Ass builder", "Arnold sanctuary", "Teer maker", "Find a girl", "Just own towel", "No scream"};
    private static final String[] addresses = {"Brno Botanická 55", "Brno Vranovská 32", "Brno Pisárky 97", "Brno Kohoutovice 32", "Brno Česká 13", "Brno Cejl 0", "Brno Komárov 54"};
    private static final String[] descriptions = {"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Vivamus porttitor turpis ac leo. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Nullam faucibus mi quis velit. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Fusce suscipit libero eget elit. Nam sed tellus id magna elementum tincidunt. Suspendisse sagittis ultrices augue. Duis risus. Maecenas lorem. Maecenas aliquet accumsan leo. Mauris dictum facilisis augue. Proin mattis lacinia justo. Fusce nibh.",
            "Maecenas ipsum velit, consectetuer eu lobortis ut, dictum at dui. In rutrum. Mauris suscipit, ligula sit amet pharetra semper, nibh ante cursus purus, vel sagittis velit mauris vel metus. Maecenas fermentum, sem in pharetra pellentesque, velit turpis volutpat ante, in pharetra metus odio a lectus. Maecenas ipsum velit, consectetuer eu lobortis ut, dictum at dui. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. Curabitur sagittis hendrerit ante. Aenean id metus id velit ullamcorper pulvinar. Vestibulum fermentum tortor id mi. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Aenean vel massa quis mauris vehicula lacinia. Donec ipsum massa, ullamcorper in, auctor et, scelerisque sed, est. Ut tempus purus at lorem. Aliquam ante.",
            "Vestibulum fermentum tortor id mi. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Nulla est. Nulla pulvinar eleifend sem. Praesent id justo in neque elementum ultrices. Etiam ligula pede, sagittis quis, interdum ultricies, scelerisque eu. Pellentesque pretium lectus id turpis. Phasellus et lorem id felis nonummy placerat. Nam sed tellus id magna elementum tincidunt. In dapibus augue non sapien. Nullam dapibus fermentum ipsum. Integer lacinia. Mauris metus. Aliquam ante. Quisque porta. Quisque tincidunt scelerisque libero. Pellentesque pretium lectus id turpis.",
            "Pellentesque sapien. Proin pede metus, vulputate nec, fermentum fringilla, vehicula vitae, justo. In laoreet, magna id viverra tincidunt, sem odio bibendum justo, vel imperdiet sapien wisi sed libero. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. Etiam quis quam. Aenean fermentum risus id tortor. Duis bibendum, lectus ut viverra rhoncus, dolor nunc faucibus libero, eget facilisis enim ipsum id lacus. Sed elit dui, pellentesque a, faucibus vel, interdum nec, diam. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Etiam egestas wisi a erat. Nullam feugiat, turpis at pulvinar vulputate, erat libero tristique tellus, nec bibendum odio risus sit amet ante. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut tempus purus at lorem. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Nullam justo enim, consectetuer nec, ullamcorper ac, vestibulum in, elit. Curabitur ligula sapien, pulvinar a vestibulum quis, facilisis vel sapien.",
            "Etiam bibendum elit eget erat. Pellentesque arcu. Duis pulvinar. Etiam ligula pede, sagittis quis, interdum ultricies, scelerisque eu. Donec quis nibh at felis congue commodo. Etiam sapien elit, consequat eget, tristique non, venenatis quis, ante. Nullam at arcu a est sollicitudin euismod. Etiam ligula pede, sagittis quis, interdum ultricies, scelerisque eu. Etiam bibendum elit eget erat. Duis ante orci, molestie vitae vehicula venenatis, tincidunt ac pede. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Curabitur ligula sapien, pulvinar a vestibulum quis, facilisis vel sapien. In sem justo, commodo ut, suscipit at, pharetra vitae, orci. Etiam quis quam. Nam sed tellus id magna elementum tincidunt. Nulla accumsan, elit sit amet varius semper, nulla mauris mollis quam, tempor suscipit diam nulla vel leo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam in lorem sit amet leo accumsan lacinia.",
            "Cras elementum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nulla non lectus sed nisl molestie malesuada. Proin pede metus, vulputate nec, fermentum fringilla, vehicula vitae, justo. Fusce consectetuer risus a nunc. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Donec quis nibh at felis congue commodo. Ut tempus purus at lorem. Aliquam ante. Nullam sapien sem, ornare ac, nonummy non, lobortis a enim. Aliquam erat volutpat. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Aliquam erat volutpat. Integer vulputate sem a nibh rutrum consequat. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Fusce dui leo, imperdiet in, aliquam sit amet, feugiat eu, orci. Sed ac dolor sit amet purus malesuada congue. Quisque tincidunt scelerisque libero.",
            "Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Aliquam ornare wisi eu metus. Phasellus et lorem id felis nonummy placerat. Duis viverra diam non justo. Nullam eget nisl. Aliquam erat volutpat. Suspendisse nisl. Nullam sapien sem, ornare ac, nonummy non, lobortis a enim. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Phasellus faucibus molestie nisl. Nullam rhoncus aliquam metus. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Pellentesque arcu. Aliquam erat volutpat. Praesent dapibus. Quisque porta. Aliquam in lorem sit amet leo accumsan lacinia."};

    public static final Gym getGym() {
        Gym gym = new Gym();
        gym.setId(randomWithRange(10000, 99999));
        gym.setName(names[randomWithRange(0, 6)]);
        gym.setDescription(descriptions[randomWithRange(0, 6)]);
        gym.setRating("" + randomWithRange(0, 50) / 10);
        gym.setPhotoPreviewUrl("http://lorempixel.com/300/150/sports?asd=" + Math.random());
        gym.setAddress(addresses[randomWithRange(0, 6)]);

        List<String> photosUrls = new ArrayList<>();
        for(int i = 0; i < randomWithRange(10, 15); i++) {
            photosUrls.add("http://lorempixel.com/300/150/sports?asd=" + Math.random());
        }
        gym.setPhotosUrls(photosUrls);
        gym.setReviews(ReviewsData.getReviews(15));

        return gym;
    }

    public static final int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
}