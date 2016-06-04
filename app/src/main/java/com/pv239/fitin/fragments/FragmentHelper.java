package com.pv239.fitin.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pv239.fitin.R;

public class FragmentHelper {

    public static void updateDisplay(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(fragment.getTag()).commit();
    }
}
