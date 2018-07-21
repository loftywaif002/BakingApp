package com.cmpny.wca.bakingapp;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cmpny.wca.bakingapp.Adapters.StepsFragmentAdapter;
import com.cmpny.wca.bakingapp.Models.Recipe;
import com.cmpny.wca.bakingapp.utilities.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeSteps extends AppCompatActivity {

    public static final String TAG = RecipeSteps.class.getSimpleName();

    @BindView(R.id.recipe_step_tab_layout)
    TabLayout mTlRecipeStep;
    @BindView(R.id.recipe_step_viewpager)
    ViewPager mVpRecipeStep;
    @BindView(android.R.id.content)
    View mParentLayout;

    private Recipe mRecipe;
    private int mStepSelectedPosition;

    public static final String RECIPE_KEY = "recipe_key";
    public static final String STEP_SELECTED_KEY = "step_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_detail_for_activity);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(RECIPE_KEY) && bundle.containsKey(STEP_SELECTED_KEY)) {
            mRecipe = bundle.getParcelable(RECIPE_KEY);
            mStepSelectedPosition = bundle.getInt(STEP_SELECTED_KEY);
        } else {
            NetworkUtils.createSnackBar(this, mParentLayout, getString(R.string.failed_to_fetch), true);
            finish();
        }

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StepsFragmentAdapter adapter = new StepsFragmentAdapter(getApplicationContext(), mRecipe.getSteps(), getSupportFragmentManager());
        mVpRecipeStep.setAdapter(adapter);
        mTlRecipeStep.setupWithViewPager(mVpRecipeStep);
        mVpRecipeStep.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (actionBar != null) {
                    actionBar.setTitle(mRecipe.getSteps().get(position).getShortDescription());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpRecipeStep.setCurrentItem(mStepSelectedPosition);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

}