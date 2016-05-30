package com.pv239.fitin.fragments.filter;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
//import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Equipment;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFiltersFragment extends Fragment {

    private Firebase ref;

    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_filters, container, false);

//        for(int i = 0; i < 10; i++) {
//            Equipment equipment = new Equipment("name" + i, "desc" + i);
//            Firebase newEquipment = this.ref.push();
//            newEquipment.setValue(equipment);
//        }

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

}