package com.example.android.donutapp;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = "MainActivity";
    protected static final String AUTHORITY = "com.example.android.DONUTAPP";
    protected static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static String json = null;
    private Cursor mCursor;
    private DbOpenHelper mDbOpenHelper;
    private Uri mUri;
    private Fragment mFragment;
    private String [] mNameArr;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private JsonObject mJsonObject;
    private JsonArray mJsonArray;
    private String [] mProjection = {
            DonutDB.DonutEntry.ID,
            DonutDB.DonutEntry.NAME
    };

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

                        if (intTime == 10) {
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

    public boolean checkToData() {
        String searchItem;
        for (int i=0; i < mJsonArray.size(); i++) {
            JsonObject obj = (JsonObject) mJsonArray.get(i);
            searchItem = obj.get("id").getAsString();

            String mSelectionClause = DonutDB.DonutEntry.ID + " = ?";
            String [] mSelectionArgs = {""};
            mSelectionArgs[0] = searchItem;
            mDbOpenHelper = new DbOpenHelper(this);

            mCursor = getContentResolver().query(
                    CONTENT_URI,
                    mProjection,
                    mSelectionClause,
                    mSelectionArgs,
                    null
            );
            if (mCursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    public void jsonToJava(String json) {
        mJsonObject = new JsonParser().parse(json).getAsJsonObject();
        mJsonArray = mJsonObject.getAsJsonArray("donut");
        ContentValues values = new ContentValues();

        if (checkToData()) {
            setTextView();
            return;
        }

        for(int i=0; i<mJsonArray.size(); i++) {
            JsonObject obj = (JsonObject) mJsonArray.get(i);
            values.put(DonutDB.DonutEntry.ID, obj.get("id").getAsString());
            values.put(DonutDB.DonutEntry.TYPE, obj.get("type").getAsString());
            values.put(DonutDB.DonutEntry.NAME, obj.get("name").getAsString());
            values.put(DonutDB.DonutEntry.PPU, obj.get("ppu").getAsString());
            mUri = getContentResolver().insert(CONTENT_URI, values);
        }
        setTextView();
    }

    public void setTextView() {
        TextView textView1 = (TextView) findViewById(R.id.id_view);;
        TextView textView2 = (TextView) findViewById(R.id.id_view2);
        TextView textView3 = (TextView) findViewById(R.id.id_view3);

        mCursor = getContentResolver().query(
                CONTENT_URI,
                mProjection,
                null,
                null,
                null
        );

        if (mCursor == null) {
            return;
        }

        int arrSize = mCursor.getCount();
        Log.d(TAG, "mCursor count = " + arrSize);
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
        Bundle bundle = new Bundle();

        if (id == R.id.cake) {
            mFragment = new MenuFragment();
            item.setTitle("Cake");
            bundle.putString("name", mNameArr[0]);
            mFragment.setArguments(bundle);
            title = "Cake";

        } else if (id == R.id.raised) {

            mFragment = new MenuFragment();
            item.setTitle("Raised");
            bundle.putString("name", mNameArr[1]);
            mFragment.setArguments(bundle);
            title = "Raised";

        } else if (id == R.id.old_fashioned) {

            mFragment = new MenuFragment();
            item.setTitle("Old Fashioned");
            bundle.putString("name", mNameArr[2]);
            mFragment.setArguments(bundle);
            title = "Old Fashioned";
        }

        setFragment(mFragment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_fragment_layout);
        frameLayout.removeAllViews();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.addToBackStack("null");
        ft.commit();
    }
}
