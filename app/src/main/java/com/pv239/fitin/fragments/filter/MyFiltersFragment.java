package com.pv239.fitin.fragments.filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Activity;
import com.pv239.fitin.Entities.Coordinates;
import com.pv239.fitin.Entities.Equipment;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.Entities.Gym;
import com.pv239.fitin.Entities.GymPreview;
import com.pv239.fitin.Entities.Review;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.FilterAdapter;
import com.pv239.fitin.adapters.GymPreviewAdapter;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.fragments.gym.GymFragment;
import com.pv239.fitin.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyFiltersFragment extends Fragment implements FilterAdapter.ItemClickCallback{

    private Firebase ref;
    private RecyclerView recyclerView;
    private FilterAdapter filterAdapter;
    private View rootView;
    private List<Filter> filterListFirebase = new ArrayList<>();

    private List<Activity> activityList = new ArrayList<>();
    private final MyFiltersFragment self = this;


    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_filters, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_filters_recycle_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //generateData();

        final Firebase filterRef = new Firebase(Constants.FIREBASE_REF + "filters");
        filterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot filterSnapshot : dataSnapshot.getChildren()) {

                    Filter filter = filterSnapshot.getValue(Filter.class);
                    filter.setId(filterSnapshot.getKey());
                    filterListFirebase.add(filter);

                }

//                Log.i(Constants.TAG, filterListFirebase.toString());
                Log.i(Constants.TAG, activityList.toString());


                if (filterAdapter == null) {
                    setDataToFragment(filterListFirebase);
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return rootView;
    }

    @Override
    public void onItemClick(int p) {

        Filter filter = filterListFirebase.get(p);

        FilterFragment filterFragment = new FilterFragment();
        filterFragment.setFilter(filter);

        FragmentHelper.updateDisplay(getFragmentManager(), filterFragment);

    }


    private void setDataToFragment (List<Filter> filterList){
        filterAdapter = new FilterAdapter(filterList, getActivity());
        recyclerView.setAdapter(filterAdapter);
        filterAdapter.setItemClickCallback(self);
    }

    private void generateData(){


        List<Review> reviewList = new ArrayList<>();
        List<String> activitiesId = new ArrayList<>();
        List<String> photoUrls = new ArrayList<>();
        List<Gym> gyms = new ArrayList<>();
        List<String> equipmentsId = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            Equipment equipment = new Equipment("name" + i, "desc" + i);
            Firebase newEquipment = this.ref.push();
            newEquipment.setValue(equipment);
            equipmentsId.add(newEquipment.getKey());
        }

        Firebase activitiesRef = new Firebase(Constants.FIREBASE_REF + "activities");

        for(int i = 0; i < 10; i++) {
            Activity activity = new Activity("name" + i, "desc" + i);
            Firebase newActivity = activitiesRef.push();
            newActivity.setValue(activity);
            activitiesId.add(newActivity.getKey());
        }

        Firebase gymsRef = new Firebase(Constants.FIREBASE_REF + "gyms");


        for(int i = 0; i < 10; i++) {
            Review review = new Review("userId" +i, "title" +i, "reviewText" +i, i, "userPhotoUrl"+i);
            reviewList.add(review);
            photoUrls.add("photoUrl" + i);
        }

        for(int i = 0; i < 10; i++) {
            Gym gym = new Gym("name" + i, "desc" + i, i, "someUrl" + i, "someAddress" +i, new Coordinates(10f*i, 10f*i), photoUrls, reviewList, Boolean.TRUE , activitiesId, equipmentsId);
            Firebase newGym = gymsRef.push();
            newGym.setValue(gym);
            gyms.add(gym);
        }


        Firebase filtersRef = new Firebase(Constants.FIREBASE_REF + "filters");
        float minX = 50.0f;
        float maxX = 100.0f;

        Random rand = new Random();

        float locationCenterX = rand.nextFloat() * (maxX - minX) + minX;
        float locationCenterY = rand.nextFloat() * (maxX - minX) + minX;
        float southWestX = rand.nextFloat() * (maxX - minX) + minX;
        float southWestY = rand.nextFloat() * (maxX - minX) + minX;
        float northEastX = rand.nextFloat() * (maxX - minX) + minX;
        float northEastY = rand.nextFloat() * (maxX - minX) + minX;

        Coordinates locationCenter = new Coordinates(locationCenterX, locationCenterY);
        Coordinates southWest = new Coordinates(southWestX, southWestY);
        Coordinates northEast = new Coordinates(northEastX, northEastY);

        for(int i = 0; i < 10; i++) {
            Filter filter = new Filter("filter" +i, gyms.get(i).getName(), locationCenter, southWest, northEast, equipmentsId, activitiesId );
            Firebase newFilter = filtersRef.push();
            newFilter.setValue(filter);
            filterListFirebase.add(filter);
        }
    }


}