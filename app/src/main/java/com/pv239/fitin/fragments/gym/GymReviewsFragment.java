package com.pv239.fitin.fragments.gym;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pv239.fitin.Entities.Gym;
import com.pv239.fitin.Entities.Review;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.ReviewAdapter;
import com.pv239.fitin.utils.Constants;

import java.util.List;

public class GymReviewsFragment extends Fragment implements ReviewAdapter.ItemClickCallback {

    private Gym gym;

    private RecyclerView recView;
    private ReviewAdapter adapter;
    private List<Review> listData;

    public GymReviewsFragment() {
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public static GymReviewsFragment newInstance(Gym gym) {
        GymReviewsFragment gymReviewsFragment = new GymReviewsFragment();
        gymReviewsFragment.setGym(gym);
        return gymReviewsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gym_reviews, container, false);

        listData = gym.getReviews();
        recView = (RecyclerView) rootView.findViewById(R.id.review_recycler_list);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ReviewAdapter(listData, getActivity());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        return rootView;
    }

    @Override
    public void onItemClick(int p) {
        Log.i(Constants.TAG, "review clicked, position: " + p);
    }
}