package com.example.android3homework7.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Pref {

    private static SharedPreferences sharedPreferences;
    public static final String PREFERENCE_NAME = "PrefName";
    private static Context context;
    public static final String PREFERENCE_KEY = "PrefKey";

    public static void init(Context context) {

        Pref.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);


    }

    public static void saveLocation(List<LatLng> place) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String list = gson.toJson(place);
        editor.putString(PREFERENCE_KEY, list).apply();
    }

    public static void clear() {
        sharedPreferences = context.getSharedPreferences("PrefKey", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();

    }

    public static List<LatLng> getLocation(List<LatLng> listLocation) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String location = sharedPreferences.getString(PREFERENCE_KEY, null);
        Type type = new TypeToken<LatLng>() {
        }.getType();
        Gson gson = new Gson();
        listLocation = gson.fromJson(location, type);
        if (listLocation == null){
            listLocation = new ArrayList<>();
        }
        return listLocation;
    }
}
