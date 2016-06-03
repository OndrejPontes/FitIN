package com.pv239.fitin.fragments.gym.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;
import com.squareup.picasso.Picasso;

public class GallerySwipeFragment extends Fragment {

    private String imageUrl;

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View swipeView = inflater.inflate(R.layout.fragment_gallery_swipe, container, false);
        ImageView imageView = (ImageView) swipeView.findViewById(R.id.gallery_swipe_image);
        Log.i(Constants.TAG, "loading " + imageUrl);
        Picasso.with(getActivity()).load(imageUrl).into(imageView);
        return swipeView;
    }
}