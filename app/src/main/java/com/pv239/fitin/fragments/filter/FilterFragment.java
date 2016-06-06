package com.pv239.fitin.fragments.filter;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

    private List<String> selectedActivityNamesList = new ArrayList<>();
    private List<String> selectedEquipmentNamesList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        final TextView filterName = (TextView) rootView.findViewById(R.id.filter_name);
        final TextView gymName = (TextView) rootView.findViewById(R.id.gym_name);

        getSelectedListData();

        Button toListButton = (Button) rootView.findViewById(R.id.toList_button);
        toListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandableListFragment listFragment = new ExpandableListFragment();
                //if there are any selected activities/equipment, send them to list fragment so we can check them accordingly
                packSelectedIfWeHaveSome();
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

    private void packSelectedIfWeHaveSome() {
        if(selectedActivityNamesList != null && selectedActivityNamesList.size() > 0) {
            DataManager.getInstance().putListObject(Constants.ACTIVITY_LIST, new ArrayList<Object>(selectedActivityNamesList));
        }
        if(selectedEquipmentNamesList != null && selectedEquipmentNamesList.size() > 0) {
            DataManager.getInstance().putListObject(Constants.EQUIPMENT_LIST, new ArrayList<Object>(selectedEquipmentNamesList));
        }
    }

    private void getSelectedListData() {
        List<Object> selectedData = DataManager.getInstance().getListObject(Constants.CHECKED_GYM_STUFF_LIST);
        if(selectedData != null && selectedData.size() > 0) {
            for(Object gymStuff : selectedData) {
                if(gymStuff instanceof Activity) {
                    selectedActivityNamesList.add(((Activity)gymStuff).getId());
                } else if (gymStuff instanceof Equipment) {
                    selectedEquipmentNamesList.add(((Equipment)gymStuff).getId());
                } else {
                    throw new IllegalArgumentException("Illegal object, impossible to convert to neither Activity, nor Equipment!");
                }
            }
        }
    }

    private Filter createFilter(String filterName, String gymName, Coordinates coordinates) {
        return null;
    }

    public void setSelectedActivityNamesList(List<String> selectedActivityNamesList) {
        this.selectedActivityNamesList = selectedActivityNamesList;
    }

    public void setSelectedEquipmentNamesList(List<String> selectedEquipmentNamesList) {
        this.selectedEquipmentNamesList = selectedEquipmentNamesList;
    }
}
