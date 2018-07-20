package com.cmpny.wca.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmpny.wca.bakingapp.Fragments.RecipeFragment;
import com.cmpny.wca.bakingapp.Models.Recipe;

public class MainActivity extends AppCompatActivity implements RecipeFragment.RecipeClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void RecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.RECIPE_KEY, recipe);
        startActivity(intent);
    }

}
