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
import com.pv239.fitin.Entities.User;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    public static final int PLACE_PICKER_REQUEST = 9002;

    private Integer filterIndex = -1;
    private Filter filter;

    private Coordinates center;
    private Coordinates upperRight;
    private Coordinates lowerLeft;

    private List<String> selectedActivityNamesList;
    private List<String> selectedEquipmentNamesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        final TextView filterNameView = (TextView) rootView.findViewById(R.id.filter_name);
        final TextView gymNameView = (TextView) rootView.findViewById(R.id.gym_name);

        //TODO: This WILL work only when we don't delete any Filter!!!!!!!! Solve fr=or delete as well
        filterIndex = (Integer) DataManager.getInstance().getObject(Constants.FILTER_INDEX);

        if(filterIndex >= 0) {
            filter = ((User) DataManager.getInstance().getObject(Constants.USER)).getFilters().get(filterIndex);
        }

        if (filter != null){
            filterNameView.setText(filter.getName());
            gymNameView.setText(filter.getGymName());
        }

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
                String filterNameValue = filterNameView.getText().toString();
                String gymNameValue = gymNameView.getText().toString();

                //get everything together and create Filter
                if (!filterNameValue.isEmpty()) {
                    upsertFilter(filterNameValue, gymNameValue);
                }
            }
        });
        return rootView;
    }

    private void packSelectedIfWeHaveSome() {
        if(selectedActivityNamesList == null) {
            selectedActivityNamesList = filter.getActivities();
        }
        if(selectedEquipmentNamesList == null) {
            selectedEquipmentNamesList = filter.getEquipments();
        }

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
                    if(selectedActivityNamesList == null) {
                        selectedActivityNamesList = new ArrayList<>();
                    }
                    selectedActivityNamesList.add(((Activity)gymStuff).getId());
                } else if (gymStuff instanceof Equipment) {
                    if(selectedEquipmentNamesList == null) {
                        selectedEquipmentNamesList = new ArrayList<>();
                    }
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

    private void upsertFilter(final String filterName, String gymName) {
        Firebase ref = new Firebase(Constants.FIREBASE_REF + "users");
        final User user = (User) DataManager.getInstance().getObject(Constants.USER);

        if (filter == null){
            filter = new Filter(filterName, gymName, center, upperRight, lowerLeft, selectedEquipmentNamesList, selectedActivityNamesList);

            int filtersCount = user.getFilters().size();

            user.getFilters().add(filter);
            ref.child(user.getId()).setValue(user);

            //TODO: Simplify this?
            //update Filter we want to see
            DataManager.getInstance().putObject(Constants.FILTER_INDEX, filtersCount);
        } else {
            filter.setName(filterName);
            filter.setGymName(gymName);
            filter.setActivities(selectedActivityNamesList);
            filter.setEquipments(selectedEquipmentNamesList);
            filter.setSouthWest(lowerLeft);
            filter.setNorthEast(upperRight);
            filter.setLocationCenter(center);

            Firebase userRef = ref.child(user.getId());
            userRef.setValue(user);

            //TODO: Change FilterIndex somehow?
        }
    }
}
