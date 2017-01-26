package com.example.android.donutapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected static final String TAG = "MainActivity";
    private static String json = null;
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private SQLiteDatabase mSQLiteDatabase;
    private DonutDB dDB;
    private DonutsDao donut;
    private Fragment fragment;
    //모든값 저장
    private ArrayList<DonutsDao> donutList = new ArrayList<>();
    //id, name 저장
    private ArrayList<DonutsDao> mStringArr = new ArrayList<>();
    private ContentResolver cr;
    private String URL = "content://com.example.android.donutapp/donut";


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

        json = loadJSONFromAsset();
        jsonToJava();

        //DB Access를 위한 인스턴스화
        mDbOpenHelper = new DbOpenHelper(this);

        try {
            //data repository write mode 획득
            mSQLiteDatabase = mDbOpenHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, "데이터베이스를 얻어올 수 없음");
            finish();
        }

        insert();
        dowhileCusor();
        intiView();
        delete();

    }

    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    public void intiView() {
        TextView mTextView1;
        TextView mTextView2;
        TextView mTextView3;
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

    //db 삽입
    public void insert() {

        if(isTable()) {
            Toast.makeText(getApplicationContext(), "중복 삽입", Toast.LENGTH_SHORT).show();
            return;
        }


        //Table에 삽입되는 레코드 객체
        ContentValues values_donut = new ContentValues();
        ContentValues values_batter = new ContentValues();
        ContentValues values_topping = new ContentValues();

        Log.d(TAG, " donutList.batter size = " + donutList.get(0).batters.batter.size());
        Log.d(TAG, " donutList.topping size = " + donutList.get(0).topping.size());

        for (int i = 0 ; i < donutList.size() ; i++) {
            values_donut.put(DonutDB.DonutEntry.ID, donutList.get(i).id);
            values_donut.put(DonutDB.DonutEntry.TYPE, donutList.get(i).type);
            values_donut.put(DonutDB.DonutEntry.NAME, donutList.get(i).name);
            values_donut.put(DonutDB.DonutEntry.PPU, donutList.get(i).ppu);

            for (int j = 0 ; j < donutList.get(i).batters.batter.size() ; j++) {
                values_batter.put(DonutDB.BatterEntry.B_ID, donutList.get(i).batters.batter.get(j).id);
                values_batter.put(DonutDB.BatterEntry.B_TYPE, donutList.get(i).batters.batter.get(j).type);
                values_batter.put(DonutDB.BatterEntry.D_ID, donutList.get(i).id);

                mSQLiteDatabase.insert(DonutDB.BatterEntry._TABLE_NAME, null, values_batter);
            }

            for (int k = 0 ; k < donutList.get(i).topping.size() ; k++) {
                values_topping.put(DonutDB.ToppingEntry.T_ID, donutList.get(i).topping.get(k).id);
                values_topping.put(DonutDB.ToppingEntry.T_TYPE, donutList.get(i).topping.get(k).type);
                values_topping.put(DonutDB.ToppingEntry.D_ID, donutList.get(i).id);

                mSQLiteDatabase.insert(DonutDB.ToppingEntry._TABLE_NAME, null, values_topping);
            }
            mSQLiteDatabase.insert(DonutDB.DonutEntry._TABLE_NAME, null, values_donut);

        }
        Toast.makeText(getApplicationContext(), "insert 완료", Toast.LENGTH_SHORT).show();

        /*
        db 확인
         */
        mCursor = mSQLiteDatabase.rawQuery("select * from donuts", null);
        if ( mCursor != null && mCursor.moveToFirst()) {
            do {
                String b_id = mCursor.getString(mCursor.getColumnIndex("id"));
                String b_type = mCursor.getString(mCursor.getColumnIndex("type"));
                String d_id = mCursor.getString(mCursor.getColumnIndex("name"));
                Log.d(TAG, "b_id : " + b_id + "     " + b_type + "     " + d_id);
            }while (mCursor.moveToNext());
        }

        mCursor.close();
    }

    //db에서 값
    private void dowhileCusor() {
        mCursor = null;
        mCursor = mSQLiteDatabase.rawQuery("select * from donuts", null);
        if (mCursor != null && mCursor.moveToFirst()) {
            do {
                donut = new DonutsDao(
                        mCursor.getString(mCursor.getColumnIndex("id")),
                        mCursor.getString(mCursor.getColumnIndex("name"))
                );
                mStringArr.add(donut);
            }while (mCursor.moveToNext());

            // check input data
            for (DonutsDao d : mStringArr) {
                Log.i(TAG, "ID = " + d.id);
                Log.i(TAG, "NAME = " + d.name);
            }



        }
        mCursor.close();
    }

    public void delete() {

//        cr.delete(Uri.parse(URL), "name=?", new String[]{"Cake"});


    }

    /*
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /*
    JsonParser로 JSON 의 최상위 노드 생성
    [반복] JsonObject/JsonArray로 원하는 노드 검색
    Gson으로 DAO객체 생성
     */
    public void jsonToJava() {
        Log.d(TAG, "josn print = " + json);

        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
        Log.d(TAG, "jsonArray = " + jsonArray);

        JsonObject[] jsonObject = new JsonObject[3];
//        ArrayList<DonutsDao> donutList = new ArrayList<>();
        donut = new DonutsDao();

        Gson gson = new Gson();
        for(int i = 0 ; i < jsonArray.size() ; i++) {
            jsonObject[i] = jsonArray.get(i).getAsJsonObject();
            donut = gson.fromJson(jsonObject[i], DonutsDao.class);
            donutList.add(donut);
        }

//        for(JsonElement o : jsonArray) {
//            DonutsDao dao = gson.fromJson(o.getAsJsonObject(), DonutsDao.class);
//            donutList.add(dao);
//        }

        Toast.makeText(getApplicationContext(), "jsonToJava 완료", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "donutList.size() : " + donutList.size());
        Log.d(TAG, "daou_id = " + donutList.get(1).id);
        Log.d(TAG, "daou_type_id = " + donutList.get(1).topping.get(3).id);
        for(int i=0 ; i < 3 ; i++) {
            Log.d(TAG, "jsonObject_" + i + " = "+ jsonObject[i]);
        }

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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = getString(R.string.app_name);

        if (id == R.id.cake) {

            fragment = new CakeFragment();
            item.setTitle(mStringArr.get(0).name);
            //fragment에 전달할 bundle 선언
            Bundle bundle = new Bundle();
            bundle.putSerializable("ArrayList", donutList);
            fragment.setArguments(bundle);
            title = "Cake";

        } else if (id == R.id.raised) {

            fragment = new RaisedFragment();
            item.setTitle(mStringArr.get(1).name);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ArrayList", donutList);
            fragment.setArguments(bundle);
            title = "Raised";

        } else if (id == R.id.old_fashioned) {

            fragment = new OldFashionedFragment();
            item.setTitle(mStringArr.get(2).name);
            Bundle bundle = new Bundle();
            bundle.putSerializable("ArrayList", donutList);
            fragment.setArguments(bundle);
            title = "Old Fashioned";
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
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
