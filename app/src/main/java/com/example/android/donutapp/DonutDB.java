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

    public static final class BatterEntry implements BaseColumns {
        public static final String B_ID = "b_id";
        public static final String B_TYPE = "b_type";
        public static final String D_ID = "d_id";

        public static final String _TABLE_NAME = "batter";
        public static final String BATTER_CREATE =
                "CREATE TABLE " + _TABLE_NAME + "("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + B_ID + " text not null , "
                        + B_TYPE + " text not null , "
                        + D_ID + " text );";
        public static final String _DELETE =
                "DROP TABLE IF EXISTS " + _TABLE_NAME;
    }

    public static final class ToppingEntry implements BaseColumns {
        public static final String T_ID = "t_id";
        public static final String T_TYPE = "t_type";
        public static final String D_ID = "d_id";

        public static final String _TABLE_NAME = "topping";
        public static final String TOPPING_CREATE =
                "CREATE TABLE " + _TABLE_NAME + "("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + T_ID +" not null , "
                        + T_TYPE +" text not null , "
                        + D_ID + " text );";
        public static final String _DELETE =
                "DROP TABLE IF EXISTS " + _TABLE_NAME;
    }
}
