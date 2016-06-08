package com.pv239.fitin.fragments.gym;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.pv239.fitin.domain.Gym;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.fragments.gym.gallery.GymGalleryFragment;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

public class GymAboutFragment extends Fragment {

    private Gym gym;

    private TextView gymAboutDescription;
    private LinearLayout favouriteLayout;
    private ImageView favouriteStar;
    private TextView favouriteText;

    public GymAboutFragment() {
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public static GymAboutFragment newInstance(Gym gym) {
        GymAboutFragment gymAboutFragment = new GymAboutFragment();
        gymAboutFragment.setGym(gym);
        return gymAboutFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gym_about, container, false);
        // Inflate the layout for this fragment
        // gym_frame_container
        GymGalleryFragment fragment = new GymGalleryFragment();
        fragment.setImageUrls(gym.getPhotosUrls());

        FragmentHelper.addFragment(getFragmentManager(), fragment, Constants.GYM_GALLERY_TAG);

        gymAboutDescription = (TextView) rootView.findViewById(R.id.gym_about_description);
        gymAboutDescription.setText(gym.getDescription());


        favouriteLayout = (LinearLayout) rootView.findViewById(R.id.gym_favourite_layout);
        favouriteStar = (ImageView) rootView.findViewById(R.id.gym_favourite_star);
        favouriteText = (TextView) rootView.findViewById(R.id.gym_favourite_text);

        if(gym.getFavourite()) {
            favouriteStar.setImageResource(R.drawable.ic_star_black_24dp);
            favouriteText.setText(R.string.remove_from_favourites);
        }

        final User user = (User) DataManager.getInstance().getObject(Constants.USER);

        favouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(Constants.TAG, "add to favourite");
                // TODO: better firebase ref
                Firebase ref = new Firebase(Constants.FIREBASE_REF + "users/" + user.getId());

                if(gym.getFavourite()) {
                    user.getFavouriteGyms().remove(gym.getId());
                    favouriteStar.setImageResource(R.drawable.ic_star_border_black_24dp);
                    favouriteText.setText(R.string.add_to_favourites);
                } else {
                    favouriteStar.setImageResource(R.drawable.ic_star_black_24dp);
                    favouriteText.setText(R.string.remove_from_favourites);
                    user.getFavouriteGyms().add(gym.getId());
                }

                gym.setFavourite(!gym.getFavourite());
                ref.setValue(user);
            }
        });

        return rootView;
    }

}
