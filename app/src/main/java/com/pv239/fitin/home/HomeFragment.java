package com.pv239.fitin.home;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pv239.fitin.R;
import com.pv239.fitin.utils.DownloadImageTask;

public class HomeFragment extends Fragment {

//    private ImageView testImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

//        testImage = (ImageView) rootView.findViewById(R.id.home_image_test);

//        new DownloadImageTask(testImage)
//                .execute("http://runt-of-the-web.com/wordpress/wp-content/uploads/2013/02/drunk-baby-meme-airplane.jpg");


        return rootView;
    }


}
