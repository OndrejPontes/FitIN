package com.pv239.fitin.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pv239.fitin.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xnieslan on 30.05.2016.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class FilterHolder extends RecyclerView.ViewHolder{
        View container;
        TextView reviewText;
        CircleImageView userProfileImage;
        TextView ratingText;
        int height;

        public FilterHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.review_container);
            reviewText= (TextView) itemView.findViewById(R.id.review_text);
            userProfileImage = (CircleImageView) itemView.findViewById(R.id.review_user_profile_image);
            ratingText = (TextView) itemView.findViewById(R.id.review_rating_text);
            height = reviewText.getHeight();
        }

    }

}
