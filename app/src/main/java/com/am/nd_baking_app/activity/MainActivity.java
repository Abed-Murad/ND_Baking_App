package com.am.nd_baking_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.am.nd_baking_app.R;
import com.am.nd_baking_app.fragment.AllRecipesFragment;
import com.am.nd_baking_app.model.Recipe;

public class MainActivity extends AppCompatActivity implements AllRecipesFragment.OnRecipeClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, RecipeInfoActivity.class);
        intent.putExtra(RecipeInfoActivity.RECIPE_KEY, recipe);
        startActivity(intent);

    }
}
