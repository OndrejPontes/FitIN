package com.pv239.fitin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pv239.fitin.Entities.Filter;
import com.pv239.fitin.Entities.GymPreview;
import com.pv239.fitin.R;

import java.util.List;

/**
 * Created by xnieslan on 30.05.2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

//    @Override
//    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return null;
//    }
//
//    @Override
//    public void onBindViewHolder(FilterHolder holder, int position) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return 0;
//    }
//
//    class FilterHolder extends RecyclerView.ViewHolder{
//        View container;
//        TextView reviewText;
//        CircleImageView userProfileImage;
//        TextView ratingText;
//        int height;
//
//        public FilterHolder(View itemView) {
//            super(itemView);
//            container = itemView.findViewById(R.id.review_container);
//            reviewText= (TextView) itemView.findViewById(R.id.review_text);
//            userProfileImage = (CircleImageView) itemView.findViewById(R.id.review_user_profile_image);
//            ratingText = (TextView) itemView.findViewById(R.id.review_rating_text);
//            height = reviewText.getHeight();
//        }
//
//    }


    private List<Filter> filterList;
    private LayoutInflater inflater;
    private Context context;

    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
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
//        Filter item = filterList.get(position);
//        holder.gymPreviewName.setText(item.getName());
//        holder.gymPreviewAddress.setText(item.getAddress());
//        holder.gymPreviewRating.setText(item.getRating());
//        Picasso.with(context)
//                .load(item.getPhotoPreviewUrl())
//                .resize(300, 150)
//                .centerCrop()
//                .into(holder.gymPreviewImage);

    }

    public void setListData(List<GymPreview> listData) {
        this.filterList.clear();
        this.filterList.addAll(filterList);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    class FilterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(getAdapterPosition());
        }
    }

}
