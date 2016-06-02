package com.pv239.fitin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pv239.fitin.Entities.Activity;
import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.Entities.GymPreview;
import com.pv239.fitin.R;

import java.util.List;

/**
 * Created by xnieslan on 30.05.2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {


    private List<Filter> filterList;
    private LayoutInflater inflater;
    private Context context;



    public FilterAdapter (List<Filter> filterList, Context context) {
        this.filterList = filterList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.my_filters_item, parent, false);
        return new FilterHolder(view);
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, int position) {
        Filter item = filterList.get(position);
        holder.filterFragmentCount.setText("4");
        holder.filterFragmentName.setText(item.getName());
        holder.filterFragmentActivities.setText(item.getActivities().get(1));
        holder.filterFragmentEquipments.setText(item.getEquipments().get(1));
    }

    public void setListData(List<Filter> listData) {
        this.filterList.clear();
        this.filterList.addAll(listData);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    class FilterHolder extends RecyclerView.ViewHolder {

        View container;
        TextView filterFragmentName;
        TextView filterFragmentEquipments;
        TextView filterFragmentActivities;
        TextView filterFragmentCount;

        public FilterHolder(View itemView) {
            super(itemView);
            filterFragmentName = (TextView) itemView.findViewById(R.id.my_filters_item_name);
            filterFragmentEquipments = (TextView) itemView.findViewById(R.id.my_filters_item_equipments);
            filterFragmentActivities = (TextView) itemView.findViewById(R.id.my_filters_item_activities);
            filterFragmentCount = (TextView) itemView.findViewById(R.id.my_filters_item_count);

            container = itemView.findViewById(R.id.my_filters_item_container);
        }

    }
}