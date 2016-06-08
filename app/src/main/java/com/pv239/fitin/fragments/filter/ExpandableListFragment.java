package com.pv239.fitin.fragments.filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.domain.Activity;
import com.pv239.fitin.domain.Equipment;
import com.pv239.fitin.domain.GymStuff;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;
import com.pv239.fitin.utils.GymStuffExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListFragment extends Fragment {

    private ExpandableListView expandableListView;
    private GymStuffExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<GymStuff>> listDataChild;

    private List<Activity> activityList = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();

    private List<Object> originalSelected = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        String name = ((String) DataManager.getInstance().getObject(Constants.FILTER_NAME));
        if(name != null) {
            getActivity().setTitle(name + " - Details");
        } else {
            getActivity().setTitle("Filter View - Details");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.gym_stuff_expendable_list_fragment, container, false);

        Firebase activityRef = new Firebase(Constants.FIREBASE_REF + "activities");
        activityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()) {
                    Activity activity = activitySnapshot.getValue(Activity.class);
                    activity.setId(activitySnapshot.getKey());
                    activityList.add(activity);
                }

                Log.i(Constants.TAG, activityList.toString());

                Firebase equipmentRef = new Firebase(Constants.FIREBASE_REF + "equipments");
                equipmentRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot equipmentSnapshot : dataSnapshot.getChildren()) {
                            Equipment equipment = equipmentSnapshot.getValue(Equipment.class);
                            equipment.setId(equipmentSnapshot.getKey());
                            equipmentList.add(equipment);
                        }

                        Log.i(Constants.TAG, equipmentList.toString());
                        prepareListData();
                        packList(rootView);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Button confirmButton = (Button) rootView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packValuesAndReturn(true);
            }
        });

        Button returnButton = (Button) rootView.findViewById(R.id.return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packValuesAndReturn(false);
            }
        });

        return rootView;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Activities");
        listDataHeader.add("Equipment");

        handleCheckedValues(Constants.ACTIVITY_LIST, new ArrayList<GymStuff>(activityList));
        handleCheckedValues(Constants.EQUIPMENT_LIST, new ArrayList<GymStuff>(equipmentList));

        listDataChild.put(listDataHeader.get(0), new ArrayList<GymStuff>(activityList)); // Header, Child data
        listDataChild.put(listDataHeader.get(1), new ArrayList<GymStuff>(equipmentList));
    }

    private void handleCheckedValues(String dataManagerConstant, List<GymStuff> list) {
        List<Object> selectedData = DataManager.getInstance().getListObject(dataManagerConstant);
        if(selectedData != null && selectedData.size() > 0) {
            for(Object gymStuffName : selectedData) {
                for(GymStuff object : list) {
                    if(object.getId().equals(gymStuffName)) {
                        object.setChecked(true);
                        originalSelected.add(object);
                    }
                }
            }
        }
    }

    private void packList(View rootView) {

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.expendable_list_view);
        listAdapter = new GymStuffExpandableListAdapter(this.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(listAdapter);

        // ListView Group click listener
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // ListView Group expanded listener
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
            }
        });

        // ListView Group collapsed listener
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        // ListView on child click listener
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
    }

    private void packValuesAndReturn(boolean packSelected) {

        if(packSelected) {
            List<Object> selectedGymStuff = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                boolean[] selectedChildren = listAdapter.getChildCheckboxStates().get(i);
                if (selectedChildren != null && selectedChildren.length > 0) {
                    for (int j = 0; j < selectedChildren.length; j++) {
                        if (selectedChildren[j]) {
                            selectedGymStuff.add(resolveCorrectObject(i, j));
                        }
                    }
                }
            }
            DataManager.getInstance().putListObject(Constants.CHECKED_GYM_STUFF_LIST, selectedGymStuff);
        } else {
            DataManager.getInstance().putListObject(Constants.CHECKED_GYM_STUFF_LIST, originalSelected);
        }

        FilterFragment filterFragment = new FilterFragment();
        FragmentHelper.replaceFragment(getFragmentManager(), filterFragment, Constants.FILTER_VIEW_TAG);
    }

    private GymStuff resolveCorrectObject(int i, int j) {
        String groupName = listDataHeader.get(i);
        return listDataChild.get(groupName).get(j);
    }
}