package com.pv239.fitin.fragments.filter;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
//import android.app.FragmentManager;
import android.os.Bundle;
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
import com.pv239.fitin.Entities.Equipment;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.Entities.Gym;
import com.pv239.fitin.Entities.Review;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.FilterAdapter;
import com.pv239.fitin.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFiltersFragment extends Fragment {

    private Firebase ref;
    private RecyclerView recyclerView;
    private FilterAdapter filterAdapter;
    private View rootView;

    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_filters, container, false);


        //generateData();

        final List<Equipment> eqList = new ArrayList<>();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot equipmentSnapshot : dataSnapshot.getChildren()) {
                    Equipment equipment = equipmentSnapshot.getValue(Equipment.class);
                    equipment.setId(equipmentSnapshot.getKey());
                    eqList.add(equipment);
                }
                Log.i(Constants.TAG, eqList.toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        return rootView;
    }

    private void setData (List<Filter> filterList){

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_filters_recycle_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        filterAdapter = new FilterAdapter(filterList, getActivity());
        recyclerView.setAdapter(filterAdapter);

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
            Gym gym = new Gym("name" + i, "desc" + i, i, "someUrl" + i, "someAddress" +i ,photoUrls, reviewList );
            Firebase newGym = gymsRef.push();
            newGym.setValue(gym);
            gyms.add(gym);
        }


        Firebase filtersRef = new Firebase(Constants.FIREBASE_REF + "filters");

        for(int i = 0; i < 10; i++) {
            Filter filter = new Filter("filter" +i, gyms.get(i).getName(), equipmentsId, activitiesId );
            Firebase newFilter = filtersRef.push();
            newFilter.setValue(filter);
        }
    }

}