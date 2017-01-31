package com.example.android.donutapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by leewoonho on 2017. 1. 21..
 */

public class RaisedAdapter extends RecyclerView.Adapter<RaisedAdapter.ViewHolder> {

    protected static final String TAG = "RaisedAdapter";
    private ArrayList<DonutsDao> mDataSet;
    private int itemLayout;
    Context context;

    public RaisedAdapter(ArrayList<DonutsDao> searchDataSet, int itemLayout) {
        this.mDataSet = searchDataSet;
        this.itemLayout = itemLayout;
    }

    public RaisedAdapter(ArrayList<DonutsDao> searchDataSet) {
        mDataSet = searchDataSet;
    }

    public RaisedAdapter(ArrayList<DonutsDao> searchDataSet, Context context, int itemLayout) {
        this.mDataSet = searchDataSet;
        this.context = context;
        this.itemLayout = itemLayout;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Log.d(TAG, "Create Success!!");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.donut_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Item setting
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d(TAG, "mIdView = " + mDataSet.get(position).id);
        holder.mIdView.setText("id = " + mDataSet.get(position).id);
        holder.mTypeView.setText("type = " + mDataSet.get(position).type);
        holder.mNameView.setText("name = " + mDataSet.get(position).name);
        holder.mPpuView.setText("ppu = " + mDataSet.get(position).ppu);
//        holder.mBatterView.setText(mDataSet.get(position).batters.batter.get(position).id);
//        holder.mToppingView.setText(mDataSet.get(position).topping.toString());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "mDataSet Size = " + mDataSet.size());
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mIdView;
        public TextView mTypeView;
        public TextView mNameView;
        public TextView mPpuView;
        public TextView mBatterView;
        public TextView mToppingView;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ViewHolder Success ");
        }
    }

}
