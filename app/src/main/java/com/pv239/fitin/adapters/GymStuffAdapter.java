package com.pv239.fitin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pv239.fitin.R;

import com.pv239.fitin.domain.GymStuff;

import java.util.List;

/**
 * Created by Admin on 08.06.2016.
 */
public class GymStuffAdapter extends RecyclerView.Adapter<GymStuffAdapter.GymActivitiesHolder> {

    private List<GymStuff> listData;
    private LayoutInflater inflater;
    private Context context;

    public GymStuffAdapter(List<GymStuff> listData, Context context) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public GymActivitiesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gym_stuff_list_item, parent, false);
        return new GymActivitiesHolder(view);
    }

    @Override
    public void onBindViewHolder(GymActivitiesHolder holder, int position) {
        GymStuff item = listData.get(position);
        holder.activityName.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class GymActivitiesHolder extends RecyclerView.ViewHolder {

        TextView activityName;

        public GymActivitiesHolder(View itemView) {
            super(itemView);
            activityName = (TextView) itemView.findViewById(R.id.gym_activity_name);
        }
    }
}
