package com.example.android.donutapp;

import static com.example.android.donutapp.DonutDB.DonutEntry.DONUT_CREATE;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by leewoonho on 2017. 1. 18..
 * Db 생성 및 업데이트
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbOpenHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Donuts.db";

    /**
    * 데이터베이스 헬퍼 생성자
    * @param context   context
    */

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DONUT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DonutDB.DonutEntry._DELETE);
        onCreate(db);
    }

}
