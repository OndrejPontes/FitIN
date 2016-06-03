package com.pv239.fitin.fragments.filter;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Activity;
import com.pv239.fitin.Entities.Coordinates;
import com.pv239.fitin.Entities.Equipment;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.Entities.GymStuff;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    private List<Activity> activityList = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        Firebase equipRef = new Firebase(Constants.FIREBASE_REF + "equipments");
        equipRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot equipmentSnapshot : dataSnapshot.getChildren()) {
                    Equipment equipment = equipmentSnapshot.getValue(Equipment.class);
                    equipment.setId(equipmentSnapshot.getKey());
                    equipmentList.add(equipment);
                }

                Log.i("Equipments", equipmentList.toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase activityRef = new Firebase(Constants.FIREBASE_REF + "activities");
        activityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()) {
                    Activity activity = activitySnapshot.getValue(Activity.class);
                    activity.setId(activitySnapshot.getKey());
                    activityList.add(activity);
                }

                Log.i("Activities", activityList.toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        final TextView filterName = (TextView) rootView.findViewById(R.id.filter_name);
        final TextView gymName = (TextView) rootView.findViewById(R.id.gym_name);

        Button toListButton = (Button) rootView.findViewById(R.id.toList_button);

        Button toMapButton = (Button) rootView.findViewById(R.id.toMap_button);

        Button confirmButton = (Button) rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filterNameValue = filterName.getText().toString();
                String gymNameValue = gymName.getText().toString();
                Coordinates coordinates = new Coordinates(48f, 15f);
                List<GymStuff> selectedStuff = new ArrayList<GymStuff>();

                //get everything together and create Filter
                if (!filterNameValue.isEmpty()) {
                    Filter newFilter = createFilter(filterNameValue, gymNameValue, coordinates, selectedStuff);
                    //TODO: do some magic with filter
                }
            }
        });
        return rootView;
    }

    private Filter createFilter(String filterName, String gymName, Coordinates coordinates, List<GymStuff> selectedGymStuff) {

        //make deep copies, save into separate lists
        List<String> selectedActivities = new ArrayList<>();
        List<String> selectedEquipment = new ArrayList<>();

        for(GymStuff gymStuff : selectedGymStuff) {
            if(gymStuff instanceof Activity) {
                selectedActivities.add(gymStuff.getName());
            } else if (gymStuff instanceof Equipment) {
                selectedEquipment.add(gymStuff.getName());
            } else {
                throw new IllegalArgumentException("Illegal object, impossible to convert to neither Activity, nor Equipment!");
            }
        }

        return new Filter(filterName, gymName, coordinates, selectedEquipment, selectedActivities);
    }
}
