package com.example.android.donutapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CakeFragment extends Fragment {

    protected static final String TAG = "CakeFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private Cursor mCursor;
    private String mName;

    public CakeFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cake, container, false);
        mContext = root.getContext();
        mRecyclerView = (RecyclerView) root.findViewById(R.id.item_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCursor = CakeList();
        mAdapter = new CakeAdapter(mContext, mCursor);
        mRecyclerView.setAdapter(mAdapter);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public Cursor CakeList() {
        String [] mProjection = {
                DonutDB.DonutEntry._ID,
                DonutDB.DonutEntry.ID,
                DonutDB.DonutEntry.TYPE,
                DonutDB.DonutEntry.NAME,
                DonutDB.DonutEntry.PPU
        };

        String mSelectionClause = DonutDB.DonutEntry.NAME + " = ?";
        String [] mSelectionArgs = {""};
        mName = getArguments().getString("name");
        mSelectionArgs[0] = mName;

        mCursor = getActivity().getContentResolver().query(
                MainActivity.CONTENT_URI,
                mProjection,
                mSelectionClause,
                mSelectionArgs,
                null
        );
        return mCursor;
    }
}