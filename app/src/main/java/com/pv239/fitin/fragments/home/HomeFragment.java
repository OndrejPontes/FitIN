package com.pv239.fitin.fragments.home;

import android.app.Activity;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.FragmentManager;
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
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.fragments.results.ResultsFragment;

public class HomeFragment extends Fragment {

    public static final int PLACE_PICKER_REQUEST = 9002;

    private View nearBy;

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
                ResultsFragment resultsFragment = new ResultsFragment();
                resultsFragment.setFilter(filter);
                resultsFragment.setRef(ref.child("gyms"));
                FragmentHelper.updateDisplay(getFragmentManager(), resultsFragment);
            }
        }
//        super.onActivityResult(requestCode, resultCode, data);
    }
}
