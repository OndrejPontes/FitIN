package com.pv239.fitin.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.pv239.fitin.R;
import com.pv239.fitin.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentHelper {

    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, String tag) {
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(tag).commit();
    }

    public static Fragment findFragment(FragmentManager fragmentManager, int fragmentTag) {
        return fragmentManager.findFragmentById(fragmentTag);
    }

    public static void removeFragment(FragmentManager fragmentManager, Fragment fragment) {
        fragmentManager.beginTransaction().remove(fragment).commit();
    }

    public static void removeFragment(FragmentManager fragmentManager, int fragmentTag) {
        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(fragmentTag)).commit();
    }

    public static List<String> getFragmentTags() {
        List<String> tagsList = Arrays.asList(
                Constants.HOME_TAG,
                Constants.FILTERS_LIST_TAG,
                Constants.FILTER_VIEW_TAG,
                Constants.FILTER_EXPAND_LIST_TAG,
                Constants.FILTER_LOCATION_TAG,
                Constants.GYMS_LIST_TAG,
                Constants.GYM_VIEW_TAG,
                Constants.GYM_GALLERY_TAG,
                Constants.FAVOURITES_TAG);
        return tagsList;
    }

    public static String getParentTag(String childFragmentTag) {
        switch (childFragmentTag) {
            case Constants.FILTER_EXPAND_LIST_TAG : {
                return Constants.FILTER_VIEW_TAG;
            }
            case Constants.FILTER_LOCATION_TAG : {
                return Constants.FILTER_VIEW_TAG;
            }
            case Constants.FILTERS_LIST_TAG : {
                return Constants.HOME_TAG;
            }
            case Constants.GYMS_LIST_TAG : {
                return Constants.FILTERS_LIST_TAG;
            }
            case Constants.GYM_VIEW_TAG : {
                return Constants.GYMS_LIST_TAG;
            }
            default: {
                return null;
            }
        }
    }
}
