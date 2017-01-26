package com.example.android.donutapp;

import static com.example.android.donutapp.DonutDB.DonutEntry.DONUT_CREATE;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by leewoonho on 2017. 1. 18..
 * Db 생성 및 업데이트
 */

public class DbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Donut.db";
    public static SQLiteDatabase mDB;

    /**
    * 데이터베이스 헬퍼 생성자
    * @param context   context
    */

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    DB 생성시 최초 1회 수행
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DONUT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DonutDB.DonutEntry._DELETE);
        onCreate(db);
    }

    public void insert() {

    }


}
