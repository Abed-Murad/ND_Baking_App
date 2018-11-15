package com.am.nd_baking_app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.am.nd_baking_app.R;
import com.am.nd_baking_app.fragment.RecipeStepDetailFragment;
import com.am.nd_baking_app.model.Step;

import java.util.List;

public class StepsFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Step> mSteps;

    public StepsFragmentPagerAdapter(Context context, List<Step> steps, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.mSteps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RecipeStepDetailFragment.KEY_STEP, mSteps.get(position));
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
