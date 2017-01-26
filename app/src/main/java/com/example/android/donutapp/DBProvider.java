package com.example.android.donutapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by leewoonho on 2017. 1. 18..
 */

public class DBProvider extends ContentProvider {

    protected static final String TAG = "DBProvider";
    private static final String DATABASE_NAME = "dunut";
    private static final int DATABASE_VERSION = 1;
    private static final String AUTHORITY = "com.example.android.DONUTAPP";
    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "donut");
    private static UriMatcher sUriMatcher;
    private SQLiteDatabase db;
    private ContentValues mValues;
    private Context context;
    private DbOpenHelper mOpenHelper;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "donut", 1);
    }

    private static final String[] mProjection = new String[] {
            DonutDB.DonutEntry._ID,
            DonutDB.DonutEntry.ID,
            DonutDB.DonutEntry.TYPE,
            DonutDB.DonutEntry.NAME,
            DonutDB.DonutEntry.PPU
    };

    private static final String mSelectionClause = null;
    private static final String[] mSelectionArgs = {};

    //Provider 생성 시 호출
    //되도록 빠르게 실행되는 초기화만 수행하는게 좋다
    @Override
    public boolean onCreate() {

        //db 설정
        context = getContext();
        mOpenHelper = new DbOpenHelper(
                getContext()
        );
        return true;
    }

    //타앱에서 쿼리를 사용할수 있도록 지정
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        Cursor c = db.query(DonutDB.DonutEntry._TABLE_NAME, null, selection, selectionArgs, null
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
        this.mValues = values;

        db = mOpenHelper.getWritableDatabase();    // 자동으로 SQLiteOpenHelper.onCreate() 메서드를 호출한다
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Uri mUri = null;
        int id = 0;
        if (sUriMatcher.match(uri) == 1) {
            id = db.delete(DonutDB.DonutEntry._TABLE_NAME, selection, selectionArgs);
            if (id > 0) {
                mUri = ContentUris.withAppendedId(CONTENT_URI, id);
                //ContentResolver 에게 바뀐 url를 알려줌
                getContext().getContentResolver().notifyChange(mUri, null);

            }else {
                Toast.makeText(getContext(), "DB삭제 실패", Toast.LENGTH_SHORT).show();
            }
        }

        return id;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
