package com.cmpny.wca.bakingapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cmpny.wca.bakingapp.Fragments.RecipeStepDetailFragment;
import com.cmpny.wca.bakingapp.Models.Steps;
import com.cmpny.wca.bakingapp.R;

import java.util.List;

public class StepsFragmentAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Steps> mSteps;

    public StepsFragmentAdapter(Context context, List<Steps> steps, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.mSteps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepDetailFragment.STEP_KEY, mSteps.get(position));
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return String.format(mContext.getString(R.string.step), position);
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }


}