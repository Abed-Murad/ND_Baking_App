package com.am.nd_baking_app.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.nd_baking_app.R;
import com.am.nd_baking_app.model.Step;
import com.am.nd_baking_app.util.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.am.nd_baking_app.databinding.RecipeStepDetailBinding;


public class RecipeStepDetailFragment extends Fragment {
    public static final String KEY_STEP = "step_k";
    private static final String KEY_POSITION = "pos_k";
    private static final String KEY_READY_TO_PLAY = "play_when_ready_k";


    private RecipeStepDetailBinding mBinding;
    private SimpleExoPlayer mExoPlayer;
    private Step mStep;

    private long mCurrentPosition = 0;
    private boolean mPlayWhenReady = true;

    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(KEY_STEP)) {
            mStep = getArguments().getParcelable(KEY_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = RecipeStepDetailBinding.inflate(R.layout.recipe_step_detail, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_POSITION)) {
            mCurrentPosition = savedInstanceState.getLong(KEY_POSITION);
            mPlayWhenReady = savedInstanceState.getBoolean(KEY_READY_TO_PLAY);
        }

        mBinding.instructionTextView.setText(mStep.getDescription());
        if (!mStep.getThumbnailURL().isEmpty()) {
            GlideApp.with(this).load(mStep.getThumbnailURL()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.recipe_placeholder).into(mBinding.stepThumbnailImageView);
            mBinding.stepThumbnailImageView.setVisibility(View.VISIBLE);
        }

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mStep.getVideoURL()))
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        else {
            // Un- hide InstructionsContainer because in case of phone landscape is hidden
            mBinding.instructionsContainerScrollView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(KEY_POSITION, mCurrentPosition);
        outState.putBoolean(KEY_READY_TO_PLAY, mPlayWhenReady);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create a default TrackSelector
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            // Bind the player to the view.
            mBinding.exoPlayerView.setPlayer(mExoPlayer);
            // Measures bandwidth during playback. Can be null if not required.
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            // Prepare the player with the source.
            mExoPlayer.prepare(videoSource);

            // onRestore
            if (mCurrentPosition != 0)
                mExoPlayer.seekTo(mCurrentPosition);

            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mBinding.exoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mCurrentPosition = mExoPlayer.getCurrentPosition();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


}
