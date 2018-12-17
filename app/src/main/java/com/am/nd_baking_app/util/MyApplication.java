package com.am.nd_baking_app.util;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.test.espresso.IdlingResource;

import com.am.nd_baking_app.BuildConfig;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MyApplication extends Application {
    // The Idling Resource which will be null in production.
    @Nullable
    private RecipesIdlingResource mIdlingResource;

    /**
     * Only called from test, creates and returns a new {@link RecipesIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    private IdlingResource initializeIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new RecipesIdlingResource();
        }
        return mIdlingResource;
    }

    public MyApplication() {

        // The IdlingResource will be null in production.
        if (BuildConfig.DEBUG) {
            initializeIdlingResource();
        }

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                // Enable logging only on debug
                return BuildConfig.DEBUG;
            }
        });

    }

    public void setIdleState(boolean state) {
        if (mIdlingResource != null)
            mIdlingResource.setIdleState(state);
    }

    @Nullable
    public RecipesIdlingResource getIdlingResource() {
        return mIdlingResource;
    }

}
