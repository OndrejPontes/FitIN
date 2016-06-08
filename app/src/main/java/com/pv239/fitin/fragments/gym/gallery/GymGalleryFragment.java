package com.pv239.fitin.fragments.gym.gallery;


import android.os.Bundle;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GymGalleryFragment extends Fragment {

    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    public static final String[] IMAGE_NAME = {"eagle", "horse", "bonobo", "wolf", "owl", "bear",};
    public List<String> imageUrls;
    static final int NUM_ITEMS = IMAGE_NAME.length;
    private int imageLenths;

    public GymGalleryFragment() {
        // Required empty public constructor
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);
        Log.i(Constants.TAG, "gallery fragment onCreateView");

        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getFragmentManager());
        viewPager = (ViewPager) rootView.findViewById(R.id.gallery_view_pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(Constants.TAG, "gallery fragment onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(Constants.TAG, "gallery fragment onResume");
    }

    public class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Fragment getItem(int position) {
//            GallerySwipeFragment fragment = new GallerySwipeFragment();
            GallerySwipeFragment swipeFragment = new GallerySwipeFragment();
            swipeFragment.setImageUrl(imageUrls.get(position));
            return swipeFragment;
        }
    }


}
