package com.pv239.fitin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.pv239.fitin.domain.Filter;
import com.pv239.fitin.R;

import java.util.List;

/**
 * Created by xnieslan on 30.05.2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {


    private List<Filter> filterList;
    private LayoutInflater inflater;
    private Context context;

    private ItemClickCallback itemClickCallback;

    private LongItemClickCallback longItemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }


    public interface LongItemClickCallback {
        void onLongItemClick(int p);
    }

    public void setLongItemClickCallback(final LongItemClickCallback longItemClickCallback) {
        this.longItemClickCallback = longItemClickCallback;
    }

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
        holder.filterFragmentName.setText(item.getName());
        if (item.getEquipments() != null && item.getEquipments().size() != 0) {
            holder.filterFragmentEquipments.setText(R.string.equipments);
        }
        if (item.getActivities() != null && item.getActivities().size() != 0) {
            holder.filterFragmentActivities.setText(R.string.activities);
        }
        if (item.getLocationCenter() != null) {
            holder.filterLocation.setText(R.string.location);
        }
    }

    public void setListData(List<Filter> listData) {
        this.filterList.clear();
        this.filterList.addAll(listData);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {

        View container;
        TextView filterFragmentName;
        TextView filterFragmentEquipments;
        TextView filterFragmentActivities;
        TextView filterLocation;

        public FilterHolder(View itemView) {
            super(itemView);
            filterFragmentName = (TextView) itemView.findViewById(R.id.my_filters_item_name);
            filterFragmentEquipments = (TextView) itemView.findViewById(R.id.my_filters_equipment);
            filterFragmentActivities = (TextView) itemView.findViewById(R.id.my_filters_activities);
            filterLocation = (TextView) itemView.findViewById(R.id.my_filters_location);

            container = itemView.findViewById(R.id.my_filters_item_container);
            container.setOnClickListener(this);
            container.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            longItemClickCallback.onLongItemClick(getAdapterPosition());
            return true;
        }
    }
}
