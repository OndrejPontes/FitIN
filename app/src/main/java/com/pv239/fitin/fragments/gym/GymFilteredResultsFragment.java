package com.pv239.fitin.fragments.gym;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.pv239.fitin.domain.Filter;
import com.pv239.fitin.domain.Gym;
import com.pv239.fitin.domain.GymPreview;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.GymPreviewAdapter;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;
import com.pv239.fitin.utils.GymFiltering;

import java.util.ArrayList;
import java.util.List;

public class GymFilteredResultsFragment extends Fragment implements GymPreviewAdapter.ItemClickCallback {

    private RecyclerView recView;
    private GymPreviewAdapter adapter;
    private List<GymPreview> listData;
    private Integer bestGymsCount;

    private Firebase ref;

    private Filter filter;

    public GymFilteredResultsFragment() {
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    public Integer getBestGymsCount() {
        return bestGymsCount;
    }

    public void setBestGymsCount(Integer bestGymsCount) {
        this.bestGymsCount = bestGymsCount;
    }

    @Override
    public void onResume() {
        super.onResume();

        loadFilterIfAny();
        String defaultText = "Filter Result";
        if (filter.getName() == null || filter.getName().isEmpty()) {
            getActivity().setTitle(defaultText);
        } else {
            getActivity().setTitle(filter.getName());
        }
    }

    private void loadFilterIfAny() {
        Integer position = (Integer) DataManager.getInstance().getObject(Constants.FILTER_INDEX);
        User u = (User) DataManager.getInstance().getObject(Constants.USER);
        filter = (position == null) ? new Filter() : u.getFilters().get(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        loadFilterIfAny();
        Firebase gymRef = ref.child("gyms");

        final GymFilteredResultsFragment self = this;

        gymRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Gym> gyms = new ArrayList<>();
                for (DataSnapshot gymSnapshot : dataSnapshot.getChildren()) {
                    Gym gym = gymSnapshot.getValue(Gym.class);
                    gym.setId(gymSnapshot.getKey());

                    // Set ids of gyms reviews
                    int i = 0;
                    for (DataSnapshot reviewSnapshot : gymSnapshot.child("reviews").getChildren()) {
                        gym.getReviews().get(i++).setId(reviewSnapshot.getKey());
                    }

                    gyms.add(gym);
                }
                gyms.size();
                listData = GymFiltering.filterGymsPreviews(filter, gyms, bestGymsCount);
                recView = (RecyclerView) rootView.findViewById(R.id.result_recycler_list);
                recView.setLayoutManager(new LinearLayoutManager(getActivity()));

                adapter = new GymPreviewAdapter(listData, getActivity());
                recView.setAdapter(adapter);
                adapter.setItemClickCallback(self);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


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

        FragmentHelper.replaceFragment(getFragmentManager(), fragment, Constants.GYM_VIEW_TAG);
    }
}
