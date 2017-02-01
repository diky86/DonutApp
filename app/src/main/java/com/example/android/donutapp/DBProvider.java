package com.example.android.donutapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leewoonho on 2017. 1. 18..
 */

public class DBProvider extends ContentProvider {

    protected static final String TAG = "DBProvider";
    private static final String DATABASE_NAME = "donuts";
    private static final int DATABASE_VERSION = 1;
    private static final String AUTHORITY = "com.example.android.DONUTAPP";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private SQLiteDatabase db;
    private Context context;
    private DbOpenHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        context = getContext();
        mOpenHelper = new DbOpenHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        db = mOpenHelper.getWritableDatabase();
        Cursor c = db.query(DATABASE_NAME, projection, selection, selectionArgs, null
        , null, sortOrder);

        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert() was called");
        db = mOpenHelper.getWritableDatabase();
        try {
            db.insert(DATABASE_NAME, "", values);
        }catch (SQLiteException e) {
            Log.d(TAG, "data insert error = " + e);
            return null;
        } finally {
            db.close();
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
