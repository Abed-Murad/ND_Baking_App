package com.am.nd_baking_app.activity;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import com.am.nd_baking_app.R;
import com.am.nd_baking_app.adapter.StepsPagerAdapter;
import com.am.nd_baking_app.model.Recipe;
import com.am.nd_baking_app.util.FUNC;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity {
    @BindView(R.id.recipe_step_tab_layout)
    TabLayout mTlRecipeStep;
    @BindView(R.id.recipe_step_viewpager)
    ViewPager mVpRecipeStep;
    @BindView(android.R.id.content)
    View mParentLayout;

    private Recipe mRecipe;
    private int mStepSelectedPosition;

    public static final String RECIPE_KEY = "recipe_k";
    public static final String STEP_SELECTED_KEY = "step_k";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(RECIPE_KEY) && bundle.containsKey(STEP_SELECTED_KEY)) {
            mRecipe = bundle.getParcelable(RECIPE_KEY);
            mStepSelectedPosition = bundle.getInt(STEP_SELECTED_KEY);
        } else {
            FUNC.makeSnackBar(this, mParentLayout, getString(R.string.failed_to_load_recipe), true);
            finish();
        }

        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StepsPagerAdapter adapter = new StepsPagerAdapter(getApplicationContext(), mRecipe.getSteps(), getSupportFragmentManager());
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
        Logger.d("onDestroy");
    }

}
