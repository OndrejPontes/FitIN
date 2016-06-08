package com.pv239.fitin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pv239.fitin.domain.Review;
import com.pv239.fitin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private List<Review> listData;
    private LayoutInflater inflater;
    private Context context;

    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public ReviewAdapter(List<Review> listData, Context context) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.review_list_item, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review item = listData.get(position);
        holder.reviewText.setText(item.getReviewText());
        holder.ratingText.setText("" + item.getRating());
        Picasso.with(context).load(item.getUserPhotoUrl()).placeholder(R.drawable.placeholder_error).into(holder.userProfileImage);
    }

    public void setListData(List<Review> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View container;
        TextView reviewText;
        CircleImageView userProfileImage;
        TextView ratingText;
        int height;

        public ReviewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.review_container);
            reviewText= (TextView) itemView.findViewById(R.id.review_text);
            userProfileImage = (CircleImageView) itemView.findViewById(R.id.review_user_profile_image);
            ratingText = (TextView) itemView.findViewById(R.id.review_rating_text);
            height = reviewText.getHeight();
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(reviewText.getHeight() > 0) {
                ViewGroup.LayoutParams params = reviewText.getLayoutParams();
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                reviewText.setLayoutParams(params);
            } else {
                reviewText.setHeight(height);
            }
            itemClickCallback.onItemClick(getAdapterPosition());
        }
    }
}
