package com.pv239.fitin.fragments.gym;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.adapters.GymStuffAdapter;
import com.pv239.fitin.domain.Activity;
import com.pv239.fitin.domain.Gym;
import com.pv239.fitin.domain.GymStuff;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.gym.gallery.GymGalleryFragment;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class GymAboutFragment extends Fragment {

    private Gym gym;

    private TextView gymAboutDescription;
    private TextView gymAboutAddress;
    private TextView gymAboutName;
    private LinearLayout favouriteLayout;
    private ImageView favouriteStar;
    private TextView favouriteText;

    // Activities
    private RecyclerView activitiesRecView;
    private GymStuffAdapter activitesAadapter;
    private List<GymStuff> activitiesListData = new ArrayList<>();
    // Equipments
    private RecyclerView equipmentsRecView;
    private GymStuffAdapter equipmentsAadapter;
    private List<GymStuff> equipmentsListData = new ArrayList<>();

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
        final View rootView = inflater.inflate(R.layout.fragment_gym_about, container, false);
        // Inflate the layout for this fragment
        // gym_frame_container
        GymGalleryFragment fragment = new GymGalleryFragment();
        fragment.setImageUrls(gym.getPhotosUrls());

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.gym_frame_container, fragment).commit();


        gymAboutName = (TextView) rootView.findViewById(R.id.gym_about_name);
        gymAboutName.setText(gym.getName());

        gymAboutDescription = (TextView) rootView.findViewById(R.id.gym_about_description);
        gymAboutDescription.setText(gym.getDescription());

        gymAboutAddress = (TextView) rootView.findViewById(R.id.gym_about_address);
        gymAboutAddress.setText(gym.getAddress());

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

        Firebase ref = new Firebase(Constants.FIREBASE_REF);
        ref.child("activities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot activitySnapshot: dataSnapshot.getChildren()) {
                    if(gym.getActivityList().contains(activitySnapshot.getKey())) {

                        Activity activity = activitySnapshot.getValue(Activity.class);
                        activity.setId(dataSnapshot.getKey());

                        activitiesListData.add(activity);
                    }
                }

                activitiesRecView = (RecyclerView) rootView.findViewById(R.id.gym_about_activities_recycler_list);
                activitiesRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

                activitesAadapter = new GymStuffAdapter(activitiesListData, getActivity());
                activitiesRecView.setAdapter(activitesAadapter);

                int dps =  activitiesListData.size() * 20;

                final float scale = getContext().getResources().getDisplayMetrics().density;
                int pixels = (int) (dps * scale + 0.5f);

                activitiesRecView.getLayoutParams().height = pixels;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        ref.child("equipments").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot equipmentsSnapshot: dataSnapshot.getChildren()) {

                    if(gym.getEquipmentList().contains(equipmentsSnapshot.getKey())) {
                        Activity activity = equipmentsSnapshot.getValue(Activity.class);
                        activity.setId(dataSnapshot.getKey());

                        equipmentsListData.add(activity);
                    }
                }

                equipmentsRecView = (RecyclerView) rootView.findViewById(R.id.gym_about_equipments_recycler_list);
                equipmentsRecView.setLayoutManager(new LinearLayoutManager(getActivity()));

                equipmentsAadapter = new GymStuffAdapter(equipmentsListData, getActivity());
                equipmentsRecView.setAdapter(equipmentsAadapter);

                int dps =  equipmentsListData.size() * 20;

                final float scale = getContext().getResources().getDisplayMetrics().density;
                int pixels = (int) (dps * scale + 0.5f);

                equipmentsRecView.getLayoutParams().height = pixels;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return rootView;
    }

}
