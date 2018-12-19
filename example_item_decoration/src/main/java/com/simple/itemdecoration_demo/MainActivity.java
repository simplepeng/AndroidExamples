package com.simple.itemdecoration_demo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<String> mItems = new ArrayList<>();
    Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new ItemDecoration2());
        mAdapter = new Adapter();
        recyclerView.setAdapter(mAdapter);

        mItems.addAll(getData());
        mAdapter.notifyDataSetChanged();

    }

    public class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_cell, viewGroup, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.tv_Cell.setText(mItems.get(i));
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_Cell;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Cell = itemView.findViewById(R.id.tv_cell);
        }
    }

    private ArrayList<String> getData() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add(String.valueOf(i));
        }
        return items;
    }

    public void add(View view) {
        ArrayList<String> data = getData();
        mItems.addAll(data);
        mAdapter.notifyItemRangeInserted(mItems.size() - data.size(), data.size());
    }
}
