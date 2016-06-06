package com.pv239.fitin.fragments.filter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.pv239.fitin.Entities.Activity;
import com.pv239.fitin.Entities.Coordinates;
import com.pv239.fitin.Entities.Equipment;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    public static final int PLACE_PICKER_REQUEST = 9002;

    private Filter filter;

    private Coordinates center;
    private Coordinates upperRight;
    private Coordinates lowerLeft;

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
                if (filter != null) {
                    packSelectedIfWeHaveSome();
                }
                FragmentHelper.updateDisplay(getFragmentManager(), listFragment);
            }
        });

        Button toMapButton = (Button) rootView.findViewById(R.id.toMap_button);
        toMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                Intent intent = null;
                try {
                    intent = builder.build(getActivity());
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        });

        Button confirmButton = (Button) rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filterNameValue = filterName.getText().toString();
                String gymNameValue = gymName.getText().toString();

                //get everything together and create Filter
                if (!filterNameValue.isEmpty()) {
                    upsertFilter(filterNameValue, gymNameValue);
                    //TODO: do some magic with filter
                }
            }
        });
        return rootView;
    }

    private void packSelectedIfWeHaveSome() {
        selectedActivityNamesList = filter.getActivities();
        selectedEquipmentNamesList = filter.getEquipments();

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == android.app.Activity.RESULT_OK) {
                center = fromPlace(PlacePicker.getPlace(getActivity(), data));
                LatLngBounds bounds = PlacePicker.getLatLngBounds(data);
                upperRight = new Coordinates(bounds.northeast.latitude, bounds.northeast.longitude);
                lowerLeft = new Coordinates(bounds.southwest.latitude, bounds.southwest.longitude);
            }
        }
    }

    private Coordinates fromPlace(Place place) {
        return new Coordinates(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    private void upsertFilter(String filterName, String gymName) {
        if (filter.getId() == null || filter.getId().isEmpty()){
            filter = new Filter(filterName, gymName, center, upperRight, lowerLeft, selectedEquipmentNamesList, selectedActivityNamesList);
            //TODO Firebase save
        } else {
            //Todo Firebase Update

            Firebase ref = new Firebase(Constants.FIREBASE_REF + "filters");
            Firebase filterRef = ref.child(filter.getId());
            //Filter updatedfilter = new Filter(filterName, gymName, center, upperRight, lowerLeft, selectedEquipmentNamesList, selectedActivityNamesList);
            //updatedfilter.setId(filter.getId());
            filter.setName(filterName);
            filterRef.setValue(filter);
        }
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
