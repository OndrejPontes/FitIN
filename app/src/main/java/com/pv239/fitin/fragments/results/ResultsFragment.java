package com.pv239.fitin.fragments.results;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.R;

public class ResultsFragment extends android.app.Fragment {

    private Filter filter;

    public ResultsFragment() {
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(filter.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        return rootView;
    }
}
