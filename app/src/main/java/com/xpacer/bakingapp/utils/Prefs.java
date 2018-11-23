package com.xpacer.bakingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xpacer.bakingapp.data.Recipe;

import static android.content.Context.MODE_PRIVATE;

public class Prefs {


    private static final String PREFS_ARGS_CURRENT_RECIPE_DATA = "current_recipe";
    private static String PREFS = "prefs";

    private SharedPreferences mPrefs;
    private static Prefs sInstance;

    private Prefs(Context context) {
        sInstance = this;
        mPrefs = context.getSharedPreferences(PREFS, MODE_PRIVATE);
    }

    public static Prefs getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Prefs(context);
        }

        return sInstance;
    }


    public void saveCurrentRecipe(@NonNull Recipe recipe) {

        mPrefs.edit().putString(PREFS_ARGS_CURRENT_RECIPE_DATA,
                new Gson().toJson(recipe, new TypeToken<Recipe>() {
                }.getType()))
                .apply();
    }

    public Recipe getCurrentRecipe() {

        String levelUpJson = mPrefs.getString(PREFS_ARGS_CURRENT_RECIPE_DATA, null);

        if (TextUtils.isEmpty(levelUpJson))
            return null;

        return new Gson().fromJson(levelUpJson, new TypeToken<Recipe>() {
        }.getType());
    }

}
