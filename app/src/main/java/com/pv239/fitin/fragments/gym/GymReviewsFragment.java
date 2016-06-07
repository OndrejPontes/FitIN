package com.pv239.fitin.fragments.gym;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.pv239.fitin.domain.Gym;
import com.pv239.fitin.domain.Review;
import com.pv239.fitin.domain.User;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.ReviewAdapter;
import com.pv239.fitin.utils.Constants;
import com.pv239.fitin.utils.DataManager;

import java.util.ArrayList;
import java.util.List;

public class GymReviewsFragment extends Fragment implements ReviewAdapter.ItemClickCallback {

    private Gym gym;

    private Review myReview;
    private boolean updateReview = false;

    private Firebase ref;

    private RecyclerView recView;
    private ReviewAdapter adapter;
    private List<Review> listData;

    // UI
    private List<ImageView> stars = new ArrayList<>();
    private EditText reviewTitle;
    private EditText reviewText;
    private Button addReview;

    public GymReviewsFragment() {
    }

    public void setGym(Gym gym) {
        this.gym = gym;

        User user = (User) DataManager.getInstance().getObject(Constants.USER);

        for(Review review : this.gym.getReviews()) {
            if(review.getUserId().equals(user.getId())) {
                this.myReview = review;
                updateReview = true;
            }
        }
        if(!updateReview) {
            this.myReview = new Review();
            this.myReview.setUserId(user.getId());
            this.myReview.setUserPhotoUrl(user.getProfileImageUrl());
            this.myReview.setRating(1);
        }
    }

    public void setRef(Firebase ref) {
        this.ref = ref;
    }

    public static GymReviewsFragment newInstance(Gym gym, Firebase ref) {
        GymReviewsFragment gymReviewsFragment = new GymReviewsFragment();
        gymReviewsFragment.setGym(gym);
        gymReviewsFragment.setRef(ref);
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

        stars.add((ImageView) rootView.findViewById(R.id.add_review_rating_star_1));
        stars.add((ImageView) rootView.findViewById(R.id.add_review_rating_star_2));
        stars.add((ImageView) rootView.findViewById(R.id.add_review_rating_star_3));
        stars.add((ImageView) rootView.findViewById(R.id.add_review_rating_star_4));
        stars.add((ImageView) rootView.findViewById(R.id.add_review_rating_star_5));

        addListenersToStars(stars);

        final Review review = this.myReview;
        reviewTitle = (EditText) rootView.findViewById(R.id.add_review_title);
        reviewText = (EditText) rootView.findViewById(R.id.add_review_text);
        addReview = (Button) rootView.findViewById(R.id.add_review_button);

        if(updateReview) {
            reviewTitle.setText(myReview.getReviewTitle());
            reviewText.setText(myReview.getReviewText());
            setStars(stars, myReview.getRating() - 1);
            addReview.setText(R.string.review_update);
        }

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                review.setReviewTitle(reviewTitle.getText().toString());
                review.setReviewText(reviewText.getText().toString());

                if(!updateReview) {
                    gym.getReviews().add(review);
                }
                ref.setValue(gym);
//                // Update review
//                if(review.getId() != null) {
//                    ref.child(review.getId()).setValue(review);
//                    addReview.setText(R.string.review_update);
//                }
//                // Create new review
//                else {
//                    Firebase newReview = ref.push();
//                    review.setId(newReview.getKey());
//                    newReview.setValue(review);
//                }

//                ref.setValue(gym.getReviews().contains());
            }
        });

        return rootView;
    }

    public void addListenersToStars(final List<ImageView> stars) {
        for(int i = 0; i < 5; i++) {
            final int finalI = i;
            stars.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStars(stars, finalI);
                }
            });
        }
    }

    public void setStars(List<ImageView> stars, int n) {
        for(int i = 0; i < 5; i++) {
            if(i <= n) {
                stars.get(i).setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                stars.get(i).setImageResource(R.drawable.ic_star_border_black_24dp);
            }
        }
        this.myReview.setRating(n+1);
    }

    @Override
    public void onItemClick(int p) {
        Log.i(Constants.TAG, "review clicked, position: " + p);
    }
}