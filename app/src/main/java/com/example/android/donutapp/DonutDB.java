package com.example.android.donutapp;

import android.provider.BaseColumns;

/**
 * Created by leewoonho on 2017. 1. 18..
 */

public final class DonutDB {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DonutDB() {}

    public static final class DonutEntry implements BaseColumns {
        public static final String CONTENT_URI = "content://com.example.android.DONUTAPP";
        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String NAME = "name";
        public static final String PPU = "ppu";
        public static final String _TABLE_NAME = "donuts";
        public static final String DONUT_CREATE =
                "CREATE TABLE " + _TABLE_NAME + "("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + ID + " text , "
                        + TYPE +" text , "
                        + NAME +" text , "
                        + PPU +" text ) ";
        public static final String _DELETE =
                "DROP TABLE IF EXISTS " + _TABLE_NAME;
    }
}
