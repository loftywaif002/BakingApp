package com.cmpny.wca.bakingapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.cmpny.wca.bakingapp.Models.Recipe;

public class Shared_Preference {
    public static final String PREFERENCES = "preferences";

    public static void saveRecipe(Context context, Recipe recipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit();
        prefs.putString(context.getString(R.string.recipe_widget_key), Recipe.toBase64String(recipe));

        prefs.apply();
    }

    public static Recipe loadRecipe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        String recipeBase64 = prefs.getString(context.getString(R.string.recipe_widget_key), "");

        return "".equals(recipeBase64) ? null : Recipe.fromBase64(prefs.getString(context.getString(R.string.recipe_widget_key), ""));
    }
}

