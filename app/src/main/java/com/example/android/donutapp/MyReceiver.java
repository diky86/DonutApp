package com.example.android.donutapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Woonho on 2017. 1. 30..
 */

public class MyReceiver extends BroadcastReceiver {

    private SharedPreferences sharedPref;
    private Context mContext;
    private Date mDate;

    private static final String TAG = "MyReceiver";
    public MyReceiver() {
        Log.d(TAG, "Receiver create");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        // 호출됐을 때 무엇을 할 것인가 정의
        Log.d(TAG, "Broadcast called");

        sharedPref = mContext.getSharedPreferences("time", 0);
        String currentTime = sharedPref.getString("CurrentTime", null);

        long now = System.currentTimeMillis();
        mDate = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        currentTime = dateFormat.format(mDate);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.commit();
        editor.putString("CurrentTime", currentTime);



    }
}
