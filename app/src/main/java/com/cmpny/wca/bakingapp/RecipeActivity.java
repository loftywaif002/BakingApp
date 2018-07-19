package com.cmpny.wca.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;

public class RecipeActivity extends AppCompatActivity {

    public static final String TAG = RecipeActivity.class.getSimpleName();
    public static final String RECIPE_KEY = "recipe_key";

    @BindView(R.id.recipe_step_list)
    RecyclerView mRecyclerView;



}
