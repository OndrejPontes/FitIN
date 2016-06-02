package com.pv239.fitin.fragments.filter;

//import android.app.Fragment;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Activity;
import com.pv239.fitin.Entities.Equipment;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private List<Equipment> equipmentList = new ArrayList<>();
    private List<Activity> activityList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);


        Firebase equipRef = new Firebase(Constants.FIREBASE_REF + "equipments");
        equipRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot equipmentSnapshot : dataSnapshot.getChildren()){
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
                for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()){
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


        List<String> list = new ArrayList<>();
        list.add("asew");
        list.add("fgtrers");
        list.add("ewter");

        Spinner spinner = (Spinner) rootView.findViewById(R.id.add_filter_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        adapter.add("asew");
        adapter.remove("asew");
        adapter.add("fgtrers");
        adapter.add("ewter");
        adapter.notifyDataSetChanged();

        spinner.setOnItemSelectedListener(this);
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(Constants.TAG, "jup");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public enum FilterParam {
        EQUIPMENT("add_filter_equipment"),
        LOCATION("add_filter_location")
        ;

        private final String text;

        FilterParam(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public class FilterType {
        
    }
}
