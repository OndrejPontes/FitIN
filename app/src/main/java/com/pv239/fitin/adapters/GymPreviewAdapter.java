package com.pv239.fitin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pv239.fitin.R;
import com.pv239.fitin.domain.GymPreview;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GymPreviewAdapter extends RecyclerView.Adapter<GymPreviewAdapter.GymPreviewHolder> {

    private List<GymPreview> listData;
    private LayoutInflater inflater;
    private Context context;

    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public GymPreviewAdapter(List<GymPreview> listData, Context context) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public GymPreviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gym_preview_list_item, parent, false);
        return new GymPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(GymPreviewHolder holder, int position) {
        GymPreview item = listData.get(position);
        holder.gymPreviewName.setText(item.getName());
        holder.gymPreviewAddress.setText(item.getAddress());
        holder.gymPreviewRating.setText("" + item.getRating());
        Picasso.with(context)
                .load(item.getPhotoPreviewUrl())
                .placeholder(R.drawable.placeholder_error)
                .resize(300, 150)
                .centerCrop()
                .into(holder.gymPreviewImage);

    }

    public void setListData(List<GymPreview> listData) {
        this.listData.clear();
        this.listData.addAll(listData);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class GymPreviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View container;
        TextView gymPreviewName;
        TextView gymPreviewAddress;
        TextView gymPreviewRating;
        ImageView gymPreviewImage;

        public GymPreviewHolder(View itemView) {
            super(itemView);
            gymPreviewName = (TextView) itemView.findViewById(R.id.gym_preview_name);
            gymPreviewAddress = (TextView) itemView.findViewById(R.id.gym_preview_address);
            gymPreviewRating = (TextView) itemView.findViewById(R.id.gym_preview_rating_text);
            gymPreviewImage = (ImageView) itemView.findViewById(R.id.gym_preview_image);

            container = itemView.findViewById(R.id.gym_preview_container);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickCallback.onItemClick(getAdapterPosition());
        }
    }
}
