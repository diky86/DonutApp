package com.example.android.donutapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = "MainActivity";
    protected static final String AUTHORITY = "com.example.android.DONUTAPP";
    protected static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static String json = null;
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private SQLiteDatabase mSQLiteDatabase;
    private DonutDB dDB;
    private DonutsDao donut;
    private Fragment fragment;
    private ArrayList<DonutsDao> donutList = new ArrayList<>();
    private ArrayList<DonutsDao> mStringArr = new ArrayList<>();
    private Uri mUri;
    private String [] mNameArr;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Date mDate;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Preference
        long time = getCurrentTime();
        sharedPref = getSharedPreferences("time", 0);
        editor = sharedPref.edit();
        editor.putLong("CurrentTime", time);
        editor.commit();

        t.start();
        loadJSONFromAsset();
    }

    long start = System.currentTimeMillis();
    Thread t = new Thread(new Runnable() {
        public void run() {
            final SimpleDateFormat sdf = new SimpleDateFormat("mmss");
            while (!(t.isInterrupted())) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        long end = System.currentTimeMillis();
                        String time = sdf.format(end - start);
                        long intTime = Long.parseLong(time);
//                        Log.d(TAG, "intTime = " + intTime);

                        if (intTime == 10) {
                            Log.d(TAG, "success");
                            setPreference();
                            start = System.currentTimeMillis();
                        }

                    }
                });
                SystemClock.sleep(100);
            }
        }
    });

    public void setPreference() {
        editor.remove("CurrentTime");
        editor.commit();
        long time = getCurrentTime();
        sharedPref = getSharedPreferences("time", 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("CurrentTime", time);
        editor.commit();
        long currentTime = sharedPref.getLong("CurrentTime", 0);
        Log.d(TAG, "Current Time = " + currentTime);
    }

    public long getCurrentTime() {
        long now = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("mmss");
        String time = dateFormat.format(now);
        long currentTime = Long.parseLong(time);
        return currentTime;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean isTable() {
        boolean isTable = false;
        mCursor = mSQLiteDatabase.rawQuery("select * from donuts", null);
        if ( mCursor != null && mCursor.moveToFirst()) {
            isTable = true;
        }
        mCursor.close();
        return isTable;
    }

//    //db 삽입
//    public void insert() {
//
//        if(isTable()) {
//            Toast.makeText(getApplicationContext(), "중복 삽입", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        //Table에 삽입되는 레코드 객체
//        ContentValues values_donut = new ContentValues();
//        ContentValues values_batter = new ContentValues();
//        ContentValues values_topping = new ContentValues();
//
//        Log.d(TAG, " donutList.batter size = " + donutList.get(0).batters.batter.size());
//        Log.d(TAG, " donutList.topping size = " + donutList.get(0).topping.size());
//
//        for (int i = 0 ; i < donutList.size() ; i++) {
//            values_donut.put(DonutDB.DonutEntry.ID, donutList.get(i).id);
//            values_donut.put(DonutDB.DonutEntry.TYPE, donutList.get(i).type);
//            values_donut.put(DonutDB.DonutEntry.NAME, donutList.get(i).name);
//            values_donut.put(DonutDB.DonutEntry.PPU, donutList.get(i).ppu);
//
//            mSQLiteDatabase.insert(DonutDB.DonutEntry._TABLE_NAME, null, values_donut);
//        }
//
//        /*
//        db 확인
//         */
//        mCursor = mSQLiteDatabase.rawQuery("select * from donuts", null);
//        if ( mCursor != null && mCursor.moveToFirst()) {
//            do {
//                String b_id = mCursor.getString(mCursor.getColumnIndex("id"));
//                String b_type = mCursor.getString(mCursor.getColumnIndex("type"));
//                String d_id = mCursor.getString(mCursor.getColumnIndex("name"));
//                Log.d(TAG, "b_id : " + b_id + "     " + b_type + "     " + d_id);
//            }while (mCursor.moveToNext());
//        }
//
//        mCursor.close();
//    }

//    //db에서 값
//    private void dowhileCusor() {
//        mCursor = null;
//        mCursor = mSQLiteDatabase.rawQuery("select * from donuts", null);
//        if (mCursor != null && mCursor.moveToFirst()) {
//            do {
//                donut = new DonutsDao(
//                        mCursor.getString(mCursor.getColumnIndex("id")),
//                        mCursor.getString(mCursor.getColumnIndex("name"))
//                );
//                mStringArr.add(donut);
//            }while (mCursor.moveToNext());
//
//            // check input data
//            for (DonutsDao d : mStringArr) {
//                Log.i(TAG, "ID = " + d.id);
//                Log.i(TAG, "NAME = " + d.name);
//            }
//        }
//        mCursor.close();
//    }

    /**
     assets에 있는 txt파일을 json형식으로 변환
     */
    public String loadJSONFromAsset() {
        try {
            InputStream is = getAssets().open("json.txt");
            int size = is.available();
            byte [] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonToJava(json);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void jsonToJava(String json) {
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("donut");
        ContentValues values = new ContentValues();

        for(int i=0; i<jsonArray.size(); i++) {
            JsonObject obj = (JsonObject) jsonArray.get(i);
            values.put(DonutDB.DonutEntry.ID, obj.get("id").getAsString());
            values.put(DonutDB.DonutEntry.TYPE, obj.get("type").getAsString());
            values.put(DonutDB.DonutEntry.NAME, obj.get("name").getAsString());
            values.put(DonutDB.DonutEntry.PPU, obj.get("ppu").getAsString());
            mUri = getContentResolver().insert(CONTENT_URI, values);
        }

        String [] mProjection = {
                DonutDB.DonutEntry.ID,
                DonutDB.DonutEntry.NAME
        };

        mCursor = getContentResolver().query(
                CONTENT_URI,
                mProjection,
                null,
                null,
                null
        );
//        if (mCursor != null && mCursor.moveToFirst()) {
//            do {
//                String id = mCursor.getString(mCursor.getColumnIndex("id"));
//            }
//            while (mCursor.moveToNext());
//        }
        setTextView();
    }

    public void setTextView() {
        TextView textView1 = (TextView) findViewById(R.id.id_view);;
        TextView textView2 = (TextView) findViewById(R.id.id_view2);
        TextView textView3 = (TextView) findViewById(R.id.id_view3);
//        textView1 = (TextView) findViewById(R.id.id_view);
//        textView2 = (TextView) findViewById(R.id.id_view2);
//        textView3 = (TextView) findViewById(R.id.id_view3);
        String [] mProjection = {
                DonutDB.DonutEntry.ID,
                DonutDB.DonutEntry.NAME
        };

        mCursor = getContentResolver().query(
                CONTENT_URI,
                mProjection,
                null,
                null,
                null
        );
        int arrSize = mCursor.getCount();
        String [] mIdArr = new String[arrSize];
        mNameArr = new String[arrSize];
        int index = 0;
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                String id = mCursor.getString(mCursor.getColumnIndex("id"));
                String name = mCursor.getString(mCursor.getColumnIndex("name"));
                mIdArr[index] = id;
                mNameArr[index] = name;
                index++;
            }
            while (mCursor.moveToNext());
        }
        textView1.setText("ID = " + mIdArr[0] + "   " +"NAME = " + mNameArr[0]);
        textView2.setText("ID = " + mIdArr[1] + "   " +"NAME = " + mNameArr[1]);
        textView3.setText("ID = " + mIdArr[2] + "   " +"NAME = " + mNameArr[2]);

        Toast.makeText(getApplicationContext(), "화면 갱신", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title = getString(R.string.app_name);
        String name;
        Bundle bundle = new Bundle();

        if (id == R.id.cake) {
            fragment = new CakeFragment();
            item.setTitle("Cake");
            bundle.putString("name", mNameArr[0]);
            fragment.setArguments(bundle);
            title = "Cake";

        } else if (id == R.id.raised) {

            fragment = new RaisedFragment();
            item.setTitle(mStringArr.get(1).name);
            //fragment에 전달할 bundle 선언
//            Bundle bundle = new Bundle();
            bundle.putSerializable("ArrayList", donutList);
            fragment.setArguments(bundle);
            title = "Raised";

        } else if (id == R.id.old_fashioned) {

            fragment = new OldFashionedFragment();
            item.setTitle(mStringArr.get(2).name);
//            Bundle bundle = new Bundle();
            bundle.putSerializable("ArrayList", donutList);
            fragment.setArguments(bundle);
            title = "Old Fashioned";
        }

        if (fragment != null) {
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_fragment_layout);
            frameLayout.removeAllViews();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.addToBackStack("null");
            ft.commit();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
