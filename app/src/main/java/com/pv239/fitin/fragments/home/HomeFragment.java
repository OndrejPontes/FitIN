package com.pv239.fitin.fragments.home;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.pv239.fitin.domain.Filter;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.fragments.gym.GymFilteredResultsFragment;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

public class HomeFragment extends Fragment {

    public static final int PLACE_PICKER_REQUEST = 9002;

    private View nearBy;
    private View best;
    private View gymsCatalog;

    private Firebase ref;

    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        nearBy = rootView.findViewById(R.id.near_by_container);
        nearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNearByClick();
            }
        });

        best = rootView.findViewById(R.id.best_gyms_container);
        best.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBestClick();
            }
        });

        gymsCatalog = rootView.findViewById(R.id.gyms_catalog_container);
        gymsCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGymsCatalogClick();
            }
        });

        return rootView;
    }

    private void onNearByClick() {
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

    private void onBestClick(){
        GymFilteredResultsFragment resultsFragment = new GymFilteredResultsFragment();
        resultsFragment.setBestGymsCount(5);
        resultsFragment.setRef(new Firebase(Constants.FIREBASE_REF));
        FragmentHelper.addFragment(getFragmentManager(), resultsFragment, Constants.GYMS_LIST_TAG);
    }

    private void onGymsCatalogClick(){
        GymFilteredResultsFragment resultsFragment = new GymFilteredResultsFragment();
        resultsFragment.setRef(new Firebase(Constants.FIREBASE_REF));
        FragmentHelper.addFragment(getFragmentManager(), resultsFragment, Constants.GYMS_LIST_TAG);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Home");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(getActivity(), data);
                // Vytvorím filter len z jednej lokácie
                Filter filter = new Filter("Near by");
                GymFilteredResultsFragment gymFilteredResultsFragment = new GymFilteredResultsFragment();
                gymFilteredResultsFragment.setFilter(filter);
                gymFilteredResultsFragment.setRef(ref.child("gyms"));
                FragmentHelper.addFragment(getFragmentManager(), gymFilteredResultsFragment, Constants.GYMS_LIST_TAG);
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }
}
