package com.pv239.fitin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TestRecyclerViewActivity extends MainActivity {

    @Bind(R.id.test_recycler_view)
    RecyclerView recyclerView;

    TestRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view_test);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);

        LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TestRecyclerViewAdapter(getData());
        recyclerView.setAdapter(adapter);

    }

    private List<String> getData(){
        return new ArrayList<String>(){{
            add("A");
            add("AA");
            add("AAA");
            add("AAAA");
            add("AAAAA");
            add("AAAAAA");
            add("AAAAAAA");
            add("AAAAAAAA");
            add("AAAAAAAAA");
        }};
    }

    public void clicked(View view) {
        adapter.addItem("INSERTED");
    }
}
