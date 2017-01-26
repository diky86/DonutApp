package com.example.android.donutapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by leewoonho on 2017. 1. 16..
 */

public class DonutsDao {

    @SerializedName("id")
    public String id;
    @SerializedName("type")
    public String type;
    @SerializedName("name")
    public String name;
    @SerializedName("ppu")
    public String ppu;
    @SerializedName("batters")
    public Batters batters;
    @SerializedName("topping")
    public ArrayList<Topping> topping;

    public DonutsDao(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DonutsDao() {
    }

    class Batters {
        @SerializedName("batter")
        public ArrayList<Batter> batter;
    }

    class Batter {
        @SerializedName("id")
        public String id;
        @SerializedName("type")
        public String type;
    }

    class Topping {
        @SerializedName("id")
        public String id;
        @SerializedName("type")
        public String type;
    }
}