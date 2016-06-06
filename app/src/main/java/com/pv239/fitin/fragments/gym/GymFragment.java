package com.pv239.fitin.fragments.gym;

import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Gym;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.GymViewPagerAdapter;
import com.pv239.fitin.dummyData.GymData;
import com.pv239.fitin.utils.Constants;


public class GymFragment extends Fragment {

    private String id;
    private String name;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Gym gym;
    private Firebase ref;

    public GymFragment() {
        Log.i(Constants.TAG, "Gym fragment constructor");
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRef(Firebase ref) {
        this.ref = ref;
        Log.i(Constants.TAG, ref.toString());
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(this.name != null) {
            getActivity().setTitle(this.name);
        } else {
            getActivity().setTitle("Gym detail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_gym, container, false);
//        ((TextView) rootView.findViewById(R.id.gym_id)).setText("Gym with id " + id);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gym gym = dataSnapshot.getValue(Gym.class);
                gym.setId(dataSnapshot.getKey());

                viewPager = (ViewPager) rootView.findViewById(R.id.gym_view_pager);
                setUpViewPager(viewPager, gym);

                tabLayout = (TabLayout) rootView.findViewById(R.id.gym_tabs);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // TODO: vytiahnuť gym podľa idčka
//        gym = GymData.getGym();
//
//        viewPager = (ViewPager) rootView.findViewById(R.id.gym_view_pager);
//        setUpViewPager(viewPager, gym);
//
//        tabLayout = (TabLayout) rootView.findViewById(R.id.gym_tabs);
//        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    private void setUpViewPager(ViewPager viewPager, Gym gym) {
        Log.i(Constants.TAG, "setUpViewPager");
        GymViewPagerAdapter adapter = new GymViewPagerAdapter(getFragmentManager());
        adapter.addFragment(GymAboutFragment.newInstance(gym), "About");
        adapter.addFragment(GymReviewsFragment.newInstance(gym, ref), "Reviews");
        viewPager.setAdapter(adapter);
    }
}