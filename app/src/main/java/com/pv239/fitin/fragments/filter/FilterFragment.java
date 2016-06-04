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
import com.pv239.fitin.MyLocationFragment;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    private List<Activity> activityList = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();

    private List<String> selectedActivityNamesList = new ArrayList<>();
    private List<String> selectedEquipmentNamesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        final Firebase equipRef = new Firebase(Constants.FIREBASE_REF + "equipments");
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

        getSelectedListData();

        Button toListButton = (Button) rootView.findViewById(R.id.toList_button);
        toListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataManager.getInstance().putListObject(Constants.ACTIVITY_LIST, new ArrayList<Object>(activityList));
                DataManager.getInstance().putListObject(Constants.EQUIPMENT_LIST, new ArrayList<Object>(equipmentList));
                ExpandableListFragment listFragment = new ExpandableListFragment();
                FragmentHelper.updateDisplay(getFragmentManager(), listFragment);
            }
        });

        Button toMapButton = (Button) rootView.findViewById(R.id.toMap_button);
        toMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLocationFragment locationFragment = new MyLocationFragment();
                FragmentHelper.updateDisplay(getFragmentManager(), locationFragment);
            }
        });

        Button confirmButton = (Button) rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filterNameValue = filterName.getText().toString();
                String gymNameValue = gymName.getText().toString();
                Coordinates coordinates = new Coordinates(48f, 15f);

                //get everything together and create Filter
                if (!filterNameValue.isEmpty()) {
                    Filter newFilter = createFilter(filterNameValue, gymNameValue, coordinates);
                    //TODO: do some magic with filter
                }
            }
        });
        return rootView;
    }

    private void getSelectedListData() {
        List<Object> selectedData = DataManager.getInstance().getListObject(Constants.CHECKED_GYM_STUFF_LIST);
        if(selectedData != null && selectedData.size() > 0) {
            for(Object gymStuff : selectedData) {
                if(gymStuff instanceof Activity) {
                    selectedActivityNamesList.add(((Activity)gymStuff).getName());
                } else if (gymStuff instanceof Equipment) {
                    selectedEquipmentNamesList.add(((Equipment)gymStuff).getName());
                } else {
                    throw new IllegalArgumentException("Illegal object, impossible to convert to neither Activity, nor Equipment!");
                }
            }
        }
    }

    private List<Object> packListData() {
        //TODO: delete, this is dummy data
        //for testing purposes
        activityList.add(new Activity("Test Activity1", "Just a test"));
        activityList.add(new Activity("Test Activity2", "Just a test"));
        activityList.add(new Activity("Test Activity3", "Just a test"));
        activityList.add(new Activity("Test Activity4", "Just a test"));
        activityList.add(new Activity("Test Activity5", "Just a test"));
        activityList.add(new Activity("Test Activity6", "Just a test"));
        activityList.add(new Activity("Test Activity7", "Just a test"));
        activityList.add(new Activity("Test Activity8", "Just a test"));
        activityList.add(new Activity("Test Activity9", "Just a test"));
        activityList.add(new Activity("Test Activity10", "Just a test"));

        equipmentList.add(new Equipment("Test Equipment1", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment2", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment3", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment4", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment5", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment6", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment7", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment8", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment9", "Just a test"));
        equipmentList.add(new Equipment("Test Equipment10", "Just a test"));

        List<Object> allStuff = new ArrayList<>();
        allStuff.addAll(activityList);
        allStuff.addAll(equipmentList);
        return allStuff;
    }

    private Filter createFilter(String filterName, String gymName, Coordinates coordinates) {

        return new Filter(filterName, gymName, coordinates, selectedEquipmentNamesList, selectedActivityNamesList);
    }
}
