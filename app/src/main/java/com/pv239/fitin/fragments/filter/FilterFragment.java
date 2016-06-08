package com.pv239.fitin.fragments.filter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLngBounds;
import com.pv239.fitin.domain.Activity;
import com.pv239.fitin.domain.Coordinates;
import com.pv239.fitin.domain.Equipment;
import com.pv239.fitin.domain.Filter;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilterFragment extends Fragment {

    public static final int PLACE_PICKER_REQUEST = 9002;

    private Integer filterIndex = -1;
    private Filter filter;

    private String filterName;
    private String gymName;

    private Coordinates location_center;
    private Coordinates location_northEast;
    private Coordinates location_southWest;

    private List<String> selectedActivityNamesList;
    private List<String> selectedEquipmentNamesList;

    @Override
    public void onResume() {
        super.onResume();
        String defaultText = "Filter View - ";
        if(filterName != null && !filterName.isEmpty()) {
            getActivity().setTitle(defaultText + filterName);
        } else {
            getActivity().setTitle(defaultText + "New Filter");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);

        final TextView filterNameView = (TextView) rootView.findViewById(R.id.filter_name);
        final TextView gymNameView = (TextView) rootView.findViewById(R.id.gym_name);
        final Button deleteButton = (Button) rootView.findViewById(R.id.delete_button);
        deleteButton.setVisibility(View.INVISIBLE);

        //TODO: This WILL work only when we don't delete any Filter!!!!!!!! Solve for delete as well
        loadTempValues();

        if(filterIndex >= 0) {
            filter = ((User) DataManager.getInstance().getObject(Constants.USER)).getFilters().get(filterIndex);
            deleteButton.setVisibility(View.VISIBLE);

            location_center = filter.getLocationCenter();
            location_northEast = filter.getNorthEast();
            location_southWest = filter.getSouthWest();
        }

        if(filterName != null && !filterName.isEmpty()) {
            filterNameView.setText(filterName);
        } else if (filter != null){
            filterNameView.setText(filter.getName());
        }
        if(gymName != null && !gymName.isEmpty()) {
            gymNameView.setText(gymName);
        } else if (filter != null){
            gymNameView.setText(filter.getGymName());
        }

        getSelectedListData();

        Button toListButton = (Button) rootView.findViewById(R.id.toList_button);
        toListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterName = saveTextViewValue(filterNameView);
                gymName = saveTextViewValue(gymNameView);
                rememberTempValues();

                ExpandableListFragment listFragment = new ExpandableListFragment();
                if (filter != null) {
                    //if there are any selected activities/equipment, send them to list fragment so we can check them accordingly
                    packSelectedIfWeHaveSome();
                }
                FragmentHelper.replaceFragment(getFragmentManager(), listFragment, Constants.FILTER_EXPAND_LIST_TAG);
            }
        });

        Button toMapButton = (Button) rootView.findViewById(R.id.toMap_button);
        toMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterName = saveTextViewValue(filterNameView);
                gymName = saveTextViewValue(gymNameView);
                rememberTempValues();

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = null;
                try {
                    intent = builder.build(getActivity());
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            }
        });

        Button confirmButton = (Button) rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterName = saveTextViewValue(filterNameView);
                gymName = saveTextViewValue(gymNameView);

                //get everything together and create Filter
                if (!filterName.isEmpty()) {
                    upsertFilter();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFilter();
            }
        });
        return rootView;
    }

    private String saveTextViewValue(TextView view) {
        return view.getText().toString();
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
                location_center = fromPlace(PlacePicker.getPlace(getActivity(), data));
                LatLngBounds bounds = PlacePicker.getLatLngBounds(data);
                location_northEast = new Coordinates(bounds.northeast.latitude, bounds.northeast.longitude);
                location_southWest = new Coordinates(bounds.southwest.latitude, bounds.southwest.longitude);
            }
        }
    }

    private void rememberTempValues() {
        DataManager.getInstance().putObject(Constants.FILTER_INDEX, filterIndex);
        DataManager.getInstance().putObject(Constants.FILTER_NAME, filterName);
        DataManager.getInstance().putObject(Constants.GYM_NAME, gymName);
        DataManager.getInstance().putObject(Constants.LOCATION_CENTER, location_center);
        DataManager.getInstance().putObject(Constants.LOCATION_NE, location_northEast);
        DataManager.getInstance().putObject(Constants.LOCATION_SW, location_southWest);
    }

    private void loadTempValues() {
        filterIndex = (Integer) DataManager.getInstance().getObject(Constants.FILTER_INDEX);
        filterName = (String) DataManager.getInstance().getObject(Constants.FILTER_NAME);
        gymName = (String) DataManager.getInstance().getObject(Constants.GYM_NAME);
        location_center = (Coordinates) DataManager.getInstance().getObject(Constants.LOCATION_CENTER);
        location_northEast = (Coordinates) DataManager.getInstance().getObject(Constants.LOCATION_NE);
        location_southWest = (Coordinates) DataManager.getInstance().getObject(Constants.LOCATION_SW);
    }

    public void invalidateTempValues() {
        DataManager.getInstance().putObject(Constants.FILTER_INDEX, -1);
        DataManager.getInstance().putObject(Constants.FILTER_NAME, null);
        DataManager.getInstance().putObject(Constants.GYM_NAME, null);
        DataManager.getInstance().putObject(Constants.LOCATION_CENTER, null);
        DataManager.getInstance().putObject(Constants.LOCATION_NE, null);
        DataManager.getInstance().putObject(Constants.LOCATION_SW, null);
    }

    private Coordinates fromPlace(Place place) {
        return new Coordinates(place.getLatLng().latitude, place.getLatLng().longitude);
    }

    private void upsertFilter() {
        Firebase ref = new Firebase(Constants.FIREBASE_REF + "users");
        final User user = (User) DataManager.getInstance().getObject(Constants.USER);

        if (filter == null){
            filter = new Filter(filterName, gymName, location_center, location_northEast, location_southWest, selectedEquipmentNamesList, selectedActivityNamesList);

            int filtersCount = user.getFilters().size();
            user.getFilters().add(filter);

            //TODO: Simplify this?
            //update Filter we want to see
            DataManager.getInstance().putObject(Constants.FILTER_INDEX, filtersCount);
        } else {
            filter.setName(filterName);
            filter.setGymName(gymName);
            filter.setActivities(selectedActivityNamesList);
            filter.setEquipments(selectedEquipmentNamesList);
            filter.setSouthWest(location_southWest);
            filter.setNorthEast(location_northEast);
            filter.setLocationCenter(location_center);

            //TODO: Change FilterIndex somehow?
        }
        ref.child(user.getId()).setValue(user);

        invalidateTempValues();

        MyFiltersFragment myFiltersFragment = new MyFiltersFragment();
        FragmentHelper.replaceFragment(getFragmentManager(), myFiltersFragment, Constants.FILTERS_LIST_TAG);
    }

    private void deleteFilter() {
        Firebase ref = new Firebase(Constants.FIREBASE_REF + "users");
        User user = (User) DataManager.getInstance().getObject(Constants.USER);

        Iterator<Filter> iterator = user.getFilters().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            iterator.next();
            if(index == filterIndex) {
                iterator.remove();
            }
            index++;
        }
        ref.child(user.getId()).setValue(user);

        invalidateTempValues();

        MyFiltersFragment myFiltersFragment = new MyFiltersFragment();
        FragmentHelper.replaceFragment(getFragmentManager(), myFiltersFragment, Constants.FILTERS_LIST_TAG);
    }
}
