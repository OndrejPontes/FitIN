package com.pv239.fitin;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by opontes on 23/03/16.
 */
public class TestRecyclerViewAdapter extends RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder> {

    private List<String> data;

    public TestRecyclerViewAdapter(List<String> data) {
        this.data = data;
    }

    public void addItem(String string){
        for (int i = 0; i < 1000000; i++) {
            data.add(string);
        }
        notifyDataSetChanged();
    }

    @Override
    public TestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setElevation(8);
            v.setTranslationZ(2);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TestRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.testText.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return (null == data ? 0 : data.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView testText;

        public ViewHolder(View view) {
            super(view);
            testText = (TextView) view.findViewById(R.id.test_text);
        }
    }
}
