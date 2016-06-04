package com.pv239.fitin.fragments.results;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.Entities.Gym;
import com.pv239.fitin.Entities.GymPreview;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.GymPreviewAdapter;
import com.pv239.fitin.dummyData.GymPreviewsData;
import com.pv239.fitin.fragments.gym.GymFragment;
import com.pv239.fitin.utils.GymFiltering;

import java.util.ArrayList;
import java.util.List;

public class ResultsFragment extends Fragment implements GymPreviewAdapter.ItemClickCallback {

    private RecyclerView recView;
    private GymPreviewAdapter adapter;
    private List<GymPreview> listData;

    private Firebase ref;

    private Filter filter;

    public ResultsFragment() {
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(filter.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        Firebase gymRef = ref.child("gyms");

        final ResultsFragment self = this;

        gymRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Gym> gyms = new ArrayList<>();
                for (DataSnapshot gymSnapshot : dataSnapshot.getChildren()) {
                    Gym gym = gymSnapshot.getValue(Gym.class);
                    gym.setId(gymSnapshot.getKey());
                    gyms.add(gym);
                }
                gyms.size();
                listData = GymFiltering.filterGymsPreviews(filter, gyms);
                recView = (RecyclerView) rootView.findViewById  (R.id.result_recycler_list);
                recView.setLayoutManager(new LinearLayoutManager(getActivity()));

                adapter = new GymPreviewAdapter(listData, getActivity());
                recView.setAdapter(adapter);
                adapter.setItemClickCallback(self);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

//        listData = GymPreviewsData.getListData();
//        recView = (RecyclerView) rootView.findViewById  (R.id.result_recycler_list);
//        recView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        adapter = new GymPreviewAdapter(GymPreviewsData.getListData(), getActivity());
//        recView.setAdapter(adapter);
//        adapter.setItemClickCallback(this);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onItemClick(int p) {
        GymPreview gymPreview = listData.get(p);

        GymFragment fragment = new GymFragment();
        fragment.setId(gymPreview.getId());
        fragment.setRef(ref.child("gyms").child(gymPreview.getId()));

        updateDisplay(fragment);
    }

    private void updateDisplay(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(fragment.getTag()).commit();
    }
}
