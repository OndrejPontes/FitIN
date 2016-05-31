package com.pv239.fitin.fragments.results;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
//import android.app.FragmentManager;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.Entities.GymPreview;
import com.pv239.fitin.R;
import com.pv239.fitin.adapters.GymPreviewAdapter;
import com.pv239.fitin.dummyData.GymPreviewsData;
import com.pv239.fitin.fragments.gym.GymFragment;

import java.util.List;

public class ResultsFragment extends Fragment implements GymPreviewAdapter.ItemClickCallback {

    private RecyclerView recView;
    private GymPreviewAdapter adapter;
    private List<GymPreview> listData;

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

        listData = GymPreviewsData.getListData();
        recView = (RecyclerView) rootView.findViewById  (R.id.result_recycler_list);
        recView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new GymPreviewAdapter(GymPreviewsData.getListData(), getActivity());
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

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

        updateDisplay(fragment);
    }

    private void updateDisplay(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(fragment.getTag()).commit();
    }
}
