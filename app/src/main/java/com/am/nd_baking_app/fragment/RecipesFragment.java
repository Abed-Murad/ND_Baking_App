package com.am.nd_baking_app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import com.am.nd_baking_app.databinding.NoDataLayoutBinding;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.am.nd_baking_app.R;
import com.am.nd_baking_app.adapter.RecipesAdapter;
import com.am.nd_baking_app.databinding.FragmentAllRecipesBinding;
import com.am.nd_baking_app.model.Recipe;
import com.am.nd_baking_app.network.RecipesApiCallback;
import com.am.nd_baking_app.network.RecipesApiManager;
import com.am.nd_baking_app.util.FUNC;
import com.am.nd_baking_app.util.Listeners;
import com.am.nd_baking_app.util.MyApplication;
import com.am.nd_baking_app.util.Prefs;
import com.am.nd_baking_app.util.SpacingItemDecoration;
import com.am.nd_baking_app.widget.AppWidgetService;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;



public class RecipesFragment extends Fragment {

    private static String KEY_RECIPES = "recipes";

    private OnRecipeClickListener mListener;
    private FragmentAllRecipesBinding mBinding;
    private NoDataLayoutBinding mNoDataBinding;
    private List<Recipe> mRecipes;
    private MyApplication myMyApplication;

    /**
     * Will load the movies when the app launch, or if the app will launch without an internet connection
     * and then reconnects, will load them without the need for user to perform a (pull to refresh)
     */
    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mRecipes == null) {
                loadRecipes();
            }
        }
    };

    public RecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment bind view to butter knife
        mBinding = FragmentAllRecipesBinding.inflate(inflater ,container, false);
        mNoDataBinding = mBinding.noDataIncludeContainer;
        mBinding.swipeRefreshLayout.setOnRefreshListener(this::loadRecipes);

        mNoDataBinding.noDataContainer.setVisibility(View.VISIBLE);
        setupRecyclerView();

        // Get the IdlingResource instance
        myMyApplication = (MyApplication) getActivity().getApplicationContext();

        myMyApplication.setIdleState(false);


        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_RECIPES)) {
            mRecipes = savedInstanceState.getParcelableArrayList(KEY_RECIPES);

            mBinding.recipesRecyclerView.setAdapter(new RecipesAdapter(getActivity().getApplicationContext(), mRecipes, new Listeners.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    mListener.onRecipeSelected(mRecipes.get(position));
                }
            }));
            dataLoadedTakeCareLayout();
        }
        return mBinding.getRoot();
    }


    private void setupRecyclerView() {
        mBinding.recipesRecyclerView.setVisibility(View.GONE);
        mBinding.recipesRecyclerView.setHasFixedSize(true);

        boolean twoPaneMode = getResources().getBoolean(R.bool.twoPaneMode);
        if (twoPaneMode) {
            mBinding.recipesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity().getApplicationContext(), 3));
        } else {
            mBinding.recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        }

        mBinding.recipesRecyclerView.addItemDecoration(new SpacingItemDecoration((int) getResources().getDimension(R.dimen.margin_medium)));
        mBinding.recipesRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecipeClickListener) {
            mListener = (OnRecipeClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRecipeClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Logger.d("onDestroyView");
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mRecipes != null && !mRecipes.isEmpty())
            outState.putParcelableArrayList(KEY_RECIPES, (ArrayList<? extends Parcelable>) mRecipes);
    }

    private void loadRecipes() {
        // Set SwipeRefreshLayout that refreshing in case that loadRecipes get called by the networkChangeReceiver
        if (FUNC.isNetworkAvailable(getActivity().getApplicationContext())) {
            mBinding.swipeRefreshLayout.setRefreshing(true);

            RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipe>>() {
                @Override
                public void onResponse(final List<Recipe> result) {
                    if (result != null) {
                        mRecipes = result;
                        mBinding.recipesRecyclerView.setAdapter(
                                new RecipesAdapter(getActivity().getApplicationContext(), mRecipes, position -> mListener.onRecipeSelected(mRecipes.get(position))));
                        // Set the default recipe for the widget
                        if (Prefs.loadRecipe(getActivity().getApplicationContext()) == null) {
                            AppWidgetService.updateWidget(getActivity(), mRecipes.get(0));
                        }

                    } else {

                        FUNC.makeSnackBar(getActivity(), getView(), getString(R.string.failed_to_load_data), true);

                    }
                    dataLoadedTakeCareLayout();

                }

                @Override
                public void onCancel() {
                    dataLoadedTakeCareLayout();
                }

            });
        } else {

            FUNC.makeSnackBar(getActivity(), getView(), getString(R.string.no_internet), true);
        }
    }

    /**
     * Check if data is loaded and show/hide Recipes RecyclerView & NoDataContainer regarding the recipes data state
     */
    private void dataLoadedTakeCareLayout() {
        boolean loaded = mRecipes != null && mRecipes.size() > 0;
        mBinding.swipeRefreshLayout.setRefreshing(false);

        mBinding.recipesRecyclerView.setVisibility(loaded ? View.VISIBLE : View.GONE);
        mNoDataBinding.noDataContainer.setVisibility(loaded ? View.GONE : View.VISIBLE);

        myMyApplication.setIdleState(true);

    }

    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }

}
