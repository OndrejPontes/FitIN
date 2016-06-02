package com.pv239.fitin.fragments.filter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Activity;
import com.pv239.fitin.Entities.Equipment;
import com.pv239.fitin.Entities.GymStuff;
import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private List<Activity> activityList = new ArrayList<>();
    private List<Equipment> equipmentList = new ArrayList<>();

    private List<Activity> checkedActivityList = new ArrayList<>();
    private List<Equipment> checkedEquipmentList = new ArrayList<>();

    private GymStuffArrayAdapter<Activity> activityListAdapter;
    private GymStuffArrayAdapter<Equipment> equipmentListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_filter, container, false);


        Firebase equipRef = new Firebase(Constants.FIREBASE_REF + "equipments");
        equipRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot equipmentSnapshot : dataSnapshot.getChildren()) {
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
                for (DataSnapshot activitySnapshot : dataSnapshot.getChildren()) {
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

        //add listeners
        ListView activity_list = (ListView) rootView.findViewById(R.id.activity_list);
        ListView equipment_list = (ListView) rootView.findViewById(R.id.equipment_list);

        // When item is tapped, toggle checked properties of CheckBox.
        activity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item,
                                     int position, long id) {
                GymStuff gymStuff = activityListAdapter.getItem( position );
                gymStuff.toggleChecked();
                GymStuffViewHolder viewHolder = (GymStuffViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked(gymStuff.isChecked());
            }
        });

        // Set our custom array adapter as the ListView's adapter.
        activityListAdapter = new GymStuffArrayAdapter<>(this.getContext(), activityList);
        activity_list.setAdapter(activityListAdapter);

        equipmentListAdapter = new GymStuffArrayAdapter<>(this.getContext(), equipmentList);
        equipment_list.setAdapter(equipmentListAdapter);

        Spinner spinner = (Spinner) rootView.findViewById(R.id.add_filter_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1);

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

    /**
     * Holds child views for one row.
     */
    private static class GymStuffViewHolder {
        private TextView nameView;
        private TextView descView;
        private CheckBox checkBox;

        public GymStuffViewHolder(TextView nameView, TextView descView, CheckBox checkBox) {
            this.nameView = nameView;
            this.descView = descView;
            this.checkBox = checkBox;
        }

        public TextView getNameView() {
            return nameView;
        }

        public void setNameView(TextView nameView) {
            this.nameView = nameView;
        }

        public TextView getDescView() {
            return descView;
        }

        public void setDescView(TextView descView) {
            this.descView = descView;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }

    /**
     * Custom adapter for displaying an array of objects.
     */
    private static class GymStuffArrayAdapter<E extends GymStuff> extends ArrayAdapter<E> {

        private LayoutInflater inflater;

        public GymStuffArrayAdapter(Context context, List<E> gymStuffList) {
            super(context, R.layout.gym_stuff_item, R.id.gym_stuff_item_name, gymStuffList);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // GymStuff to display
            E gymStuff = (E) this.getItem(position);

            // The child views in each row.
            CheckBox checkBox;
            TextView nameView;
            TextView descView;

            // Create a new row view
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gym_stuff_item, null);

                // Find the child views.
                nameView = (TextView) convertView.findViewById(R.id.gym_stuff_item_name);
                descView = (TextView) convertView.findViewById(R.id.gym_stuff_item_desc);
                checkBox = (CheckBox) convertView.findViewById(R.id.gym_stuff_item_checkbox);

                // Optimization: Tag the row with it's child views, so we don't have to
                // call findViewById() later when we reuse the row.
                convertView.setTag(new GymStuffViewHolder(nameView, descView, checkBox));

                // If CheckBox is toggled, update the GymStuff it is tagged with.
                checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox checkBox1 = (CheckBox) v;
                        E gymStuff1 = (E) checkBox1.getTag();
                        gymStuff1.setChecked(checkBox1.isChecked());
                    }
                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                GymStuffViewHolder viewHolder = (GymStuffViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox();
                nameView = viewHolder.getNameView();
                descView = viewHolder.getDescView();
            }

            // Tag the CheckBox with the GymStuff it is displaying, so that we can
            // access the GymStuff in onClick() when the CheckBox is toggled.
            checkBox.setTag(gymStuff);

            // Display GymStuff data
            checkBox.setChecked(gymStuff.isChecked());
            nameView.setText(gymStuff.getName());
            descView.setText(gymStuff.getDescription());

            return convertView;
        }
    }
}
