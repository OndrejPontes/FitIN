package com.pv239.fitin.fragments.filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.pv239.fitin.domain.Activity;
import com.pv239.fitin.domain.Coordinates;
import com.pv239.fitin.domain.Equipment;
import com.pv239.fitin.domain.Filter;
import com.pv239.fitin.domain.Gym;
import com.pv239.fitin.domain.Review;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.FilterAdapter;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.fragments.gym.GymFilteredResultsFragment;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyFiltersFragment extends Fragment implements FilterAdapter.ItemClickCallback, FilterAdapter.LongItemClickCallback{

    private Firebase ref;
    private RecyclerView recyclerView;
    private FilterAdapter filterAdapter;
    private View rootView;
    private List<Filter> filterListFirebase = new ArrayList<>();
    private final MyFiltersFragment self = this;


    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Filters List");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.my_filters, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_filters_recycle_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //generateData();
        User user = (User) DataManager.getInstance().getObject(Constants.USER);

        if (filterAdapter == null) {
            setDataToFragment(user.getFilters());
        }
        return rootView;
    }

    @Override
    public void onItemClick(int p) {

        Filter filter = new Filter("Near by");

        GymFilteredResultsFragment gymFilteredResultsFragment = new GymFilteredResultsFragment();
        gymFilteredResultsFragment.setFilter(filter);
        gymFilteredResultsFragment.setRef(new Firebase(Constants.FIREBASE_REF));

        DataManager.getInstance().putObject(Constants.FILTER_INDEX, p);
        FragmentHelper.replaceFragment(getFragmentManager(), gymFilteredResultsFragment, Constants.GYMS_LIST_TAG);
        Log.i(Constants.TAG, "onItemClick");

    }

    @Override
    public void onLongItemClick(int p) {
        FilterFragment filterFragment = new FilterFragment();

        DataManager.getInstance().putObject(Constants.FILTER_INDEX, p);
        FragmentHelper.replaceFragment(getFragmentManager(), filterFragment, Constants.FILTER_VIEW_TAG);
        Log.i(Constants.TAG, "onLongItemClick");
    }

    private void setDataToFragment (List<Filter> filterList) {
        filterAdapter = new FilterAdapter(filterList, getActivity());
        recyclerView.setAdapter(filterAdapter);
        filterAdapter.setItemClickCallback(self);
        filterAdapter.setLongItemClickCallback(self);
    }

    private void generateData() {
        List<Review> reviewList = new ArrayList<>();
        List<String> activitiesId = new ArrayList<>();
        List<String> photoUrls = new ArrayList<>();
        List<Gym> gyms = new ArrayList<>();
        List<String> gymsId = new ArrayList<>();
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
            gymsId.add(gym.getId());
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

        Firebase usersRef = new Firebase(Constants.FIREBASE_REF + "users");

        for(int i = 0; i < 2; i++) {
            User user = new User("password", "userName" + i, i+ "@email.com", "profileImageUrl" + i, filterListFirebase, gymsId);
            Firebase newUser = usersRef.push();
            newUser.setValue(user);
        }
    }



}