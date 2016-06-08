package com.pv239.fitin.fragments.favourite;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
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
import com.pv239.fitin.domain.Filter;
import com.pv239.fitin.domain.Gym;
import com.pv239.fitin.domain.GymPreview;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.GymPreviewAdapter;
import com.pv239.fitin.fragments.FragmentHelper;
import com.pv239.fitin.fragments.gym.GymFragment;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;
import com.pv239.fitin.utils.GymFiltering;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment implements GymPreviewAdapter.ItemClickCallback {

    private RecyclerView recView;
    private GymPreviewAdapter adapter;
    private List<GymPreview> listData;
    private List<String> favGymsId;
    private User user;

    private Firebase ref = new Firebase(Constants.FIREBASE_REF);

    private Filter filter;

    public FavouriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        user = (User) DataManager.getInstance().getObject(Constants.USER);
        favGymsId = user.getFavouriteGyms();
        Firebase gymRef = ref.child("gyms");

        final FavouriteFragment self = this;


        gymRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Gym> gyms = new ArrayList<>();
                for (DataSnapshot gymSnapshot : dataSnapshot.getChildren()) {
                    Gym gym = gymSnapshot.getValue(Gym.class);
                    gym.setId(gymSnapshot.getKey());

                    if (favGymsId.contains(gym.getId())) {
                        gyms.add(gym);
                    }
                }

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


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Favourites");
    }



    @Override
    public void onItemClick(int p) {
        GymPreview gymPreview = listData.get(p);

        GymFragment fragment = new GymFragment();
        fragment.setId(gymPreview.getId());
        fragment.setRef(ref.child("gyms").child(gymPreview.getId()));

        FragmentHelper.addFragment(getFragmentManager(), fragment, Constants.GYM_VIEW_TAG);
    }
}
