package com.pv239.fitin.fragments.gym;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pv239.fitin.Entities.Gym;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.gym.gallery.GalleryFragment;

public class GymAboutFragment extends Fragment {

    private Gym gym;

    private TextView gymAboutDescription;

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
        GalleryFragment fragment = new GalleryFragment();
        fragment.setImageUrls(gym.getPhotosUrls());

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.gym_frame_container, fragment).commit();

        gymAboutDescription = (TextView) rootView.findViewById(R.id.gym_about_description);
        gymAboutDescription.setText(gym.getDescription());

        return rootView;
    }

}
