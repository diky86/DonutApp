package com.example.android.donutapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by leewoonho on 2017. 1. 21..
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    protected static final String TAG = "ItemAdapter";
    private Context mContext;
    private Cursor mCursor;

    public ItemAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    // Item setting
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        holder.mIdView.setText("ID = " + mCursor.getString(mCursor.getColumnIndex("id")));
        holder.mTypeView.setText("TYPE = " + mCursor.getString(mCursor.getColumnIndex("type")));
        holder.mNameView.setText("NAME = " + mCursor.getString(mCursor.getColumnIndex("name")));
        holder.mPpuView.setText("PPU = " + mCursor.getString(mCursor.getColumnIndex("ppu")));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mIdView;
        public TextView mTypeView;
        public TextView mNameView;
        public TextView mPpuView;

        public ViewHolder(View itemView) {
            super(itemView);
            mIdView = (TextView) itemView.findViewById(R.id.item_view_id);
            mTypeView = (TextView) itemView.findViewById(R.id.item_view_type);
            mNameView = (TextView) itemView.findViewById(R.id.item_view_name);
            mPpuView = (TextView) itemView.findViewById(R.id.item_view_ppu);

        }
    }
}