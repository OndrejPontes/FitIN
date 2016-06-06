package com.pv239.fitin.fragments.filter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Activity;
import com.pv239.fitin.Entities.Equipment;
import com.pv239.fitin.Entities.GymStuff;
import com.pv239.fitin.R;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableListFragment extends Fragment {

    private ExpandableListView expandableListView;
    private GymStuffExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String, List<GymStuff>> listDataChild;

    private List<Activity> activityList = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();

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

        listDataChild.put(listDataHeader.get(0), new ArrayList<GymStuff>(activityList)); // Header, Child data
        listDataChild.put(listDataHeader.get(1), new ArrayList<GymStuff>(equipmentList));
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
                boolean[] selectedChildren = listAdapter.childCheckboxStates.get(i);
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
            DataManager.getInstance().putListObject(Constants.CHECKED_GYM_STUFF_LIST, new ArrayList<>());
        }

        FilterFragment filterFragment = new FilterFragment();
        FragmentHelper.updateDisplay(getFragmentManager(), filterFragment);
    }

    private GymStuff resolveCorrectObject(int i, int j) {
        String groupName = listDataHeader.get(i);
        return listDataChild.get(groupName).get(j);
    }

    /**
     * DON'T EVEN THINK ABOUT MODIFYING ANY OF THE FOLLOWING CODE!
     * It is black magic found in dark corners of the Internet
     * and it took me 2 hours to tailor it to our needs after
     * spending 3 hours finding something for this problem.
     * <p/>
     * Live with assertion that it is working flawlessly.
     */
    private class GymStuffExpandableListAdapter extends BaseExpandableListAdapter {

        // Define activity context
        private Context mContext;

        /*
         * Here we have a Hashmap containing a String key
         * (can be Integer or other type but I was testing
         * with contacts so I used contact name as the key)
        */
        private Map<String, List<GymStuff>> mapOfChildrenForGroupKey = new HashMap<>();

        // ArrayList that is what each key in the above
        // hashmap points to
        private List<String> listOfGroupNames = new ArrayList<>();

        // Hashmap for keeping track of our checkbox check states
        private Map<Integer, boolean[]> childCheckboxStates = new HashMap<>();

        // Our getChildView & getGroupView use the viewholder patter
        // Here are the viewholders defined, the inner classes are
        // at the bottom
        private ChildViewHolder childViewHolder;
        private GroupViewHolder groupViewHolder;

        /*
         *  For the purpose of this document, I'm only using a single
         *  textview in the group (parent) and child, but you're limited only
         *  by your XML view for each group item :)
        */
        private String groupText;
        private String childText;

        /*  Here's the constructor we'll use to pass in our calling
         *  activity's context, group items, and child items
        */
        public GymStuffExpandableListAdapter(Context context, List<String> listDataGroup, Map<String, List<GymStuff>> listDataChild) {

            mContext = context;
            listOfGroupNames = listDataGroup;
            mapOfChildrenForGroupKey = listDataChild;

            // Initialize our hashmap containing our check states here
            childCheckboxStates = new HashMap<>();
        }

        @Override
        public int getGroupCount() {
            return listOfGroupNames.size();
        }

        /*
         * This defaults to "public object getGroup" if you auto import the methods
         * I always make a point to change it from "object" to whatever item
         * I passed through the constructor
        */
        @Override
        public String getGroup(int groupPosition) {
            return listOfGroupNames.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            //  I passed a text string into an activity holding a getter/setter
            //  which I passed in through "ExpListGroupItems".
            //  Here is where I call the getter to get that text
            groupText = getGroup(groupPosition);

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.gym_stuff_group_item, null);

                // Initialize the GroupViewHolder defined at the bottom of this document
                groupViewHolder = new GroupViewHolder();

                groupViewHolder.groupName = (TextView) convertView.findViewById(R.id.gym_stuff_group_item_name);

                convertView.setTag(groupViewHolder);
            } else {

                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }

            groupViewHolder.groupName.setText(groupText);

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mapOfChildrenForGroupKey.get(listOfGroupNames.get(groupPosition)).size();
        }

        /*
         * This defaults to "public object getChild" if you auto import the methods
         * I always make a point to change it from "object" to whatever item
         * I passed through the constructor
        */
        @Override
        public GymStuff getChild(int groupPosition, int childPosition) {
            return mapOfChildrenForGroupKey.get(listOfGroupNames.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final int mGroupPosition = groupPosition;
            final int mChildPosition = childPosition;

            //  I passed a text string into an activity holding a getter/setter
            //  which I passed in through "ExpListChildItems".
            //  Here is where I call the getter to get that text
            childText = getChild(mGroupPosition, mChildPosition).getName();

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.gym_stuff_item, null);

                childViewHolder = new ChildViewHolder();

                childViewHolder.name = (TextView) convertView
                        .findViewById(R.id.gym_stuff_item_name);

                childViewHolder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.gym_stuff_item_checkbox);

                convertView.setTag(R.layout.gym_stuff_item, childViewHolder);

            } else {

                childViewHolder = (ChildViewHolder) convertView
                        .getTag(R.layout.gym_stuff_item);
            }

            childViewHolder.name.setText(childText);
            /*
         * You have to set the onCheckChangedListener to null
         * before restoring check states because each call to
         * "setChecked" is accompanied by a call to the
         * onCheckChangedListener
        */
            childViewHolder.checkBox.setOnCheckedChangeListener(null);

            if (childCheckboxStates.containsKey(mGroupPosition)) {
            /*
             * if the hashmap childCheckboxStates<Integer, Boolean[]> contains
             * the value of the parent view (group) of this child (aka, the key),
             * then retrive the boolean array getChecked[]
            */
                boolean getChecked[] = childCheckboxStates.get(mGroupPosition);

                // set the check state of this position's checkbox based on the
                // boolean value of getChecked[position]
                childViewHolder.checkBox.setChecked(getChecked[mChildPosition]);

            } else {

            /*
            * if the hashmap childCheckboxStates<Integer, Boolean[]> does not
            * contain the value of the parent view (group) of this child (aka, the key),
            * (aka, the key), then initialize getChecked[] as a new boolean array
            *  and set it's size to the total number of children associated with
            *  the parent group
            */
                boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];

                // add getChecked[] to the childCheckboxStates hashmap using mGroupPosition as the key
                childCheckboxStates.put(mGroupPosition, getChecked);

                // set the check state of this position's checkbox based on the
                // boolean value of getChecked[position]
                childViewHolder.checkBox.setChecked(false);
            }

            childViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {

                    if (isChecked) {

                        boolean getChecked[] = childCheckboxStates.get(mGroupPosition);
                        getChecked[mChildPosition] = isChecked;
                        childCheckboxStates.put(mGroupPosition, getChecked);

                    } else {

                        boolean getChecked[] = childCheckboxStates.get(mGroupPosition);
                        getChecked[mChildPosition] = isChecked;
                        childCheckboxStates.put(mGroupPosition, getChecked);
                    }
                }
            });

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        public Map<Integer, boolean[]> getChildCheckboxStates() {
            return childCheckboxStates;
        }

        private final class GroupViewHolder {

            TextView groupName;
        }

        private final class ChildViewHolder {

            TextView name;
            CheckBox checkBox;
        }
    }
}