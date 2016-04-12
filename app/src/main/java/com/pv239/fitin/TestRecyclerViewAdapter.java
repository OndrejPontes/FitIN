package com.pv239.fitin;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        for (int i = 0; i < 1000; i++) {
            data.add(string);
        }
        notifyDataSetChanged();
    }

    @Override
    public TestRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v ;

        switch(viewType){
            case 0:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_1, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_2, parent, false);
                break;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            v.setElevation(8);
            v.setTranslationZ(2);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TestRecyclerViewAdapter.ViewHolder holder, int position) {
//        holder.testText.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return (null == data ? 0 : data.size());
    }

    @Override
    public int getItemViewType(int position) {
        return position%2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView testText;

        public ViewHolder(View view) {
            super(view);
//            testText = (TextView) view.findViewById(R.id.test_text);
        }
    }
}
