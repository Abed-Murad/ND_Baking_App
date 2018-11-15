package com.am.nd_baking_app.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepDetailFragment extends Fragment {
    public static final String KEY_STEP = "step_k";
    private static final String KEY_POSITION = "pos_k";
    private static final String KEY_READY_TO_PLAY = "play_when_ready_k";

    @BindView(R.id.instructions_container)
    NestedScrollView mInstructionsContainer;
    @BindView(R.id.exo_player_view)
    PlayerView mExoPlayerView;
    @BindView(R.id.step_thumbnail_image)
    ImageView mIvThumbnail;
    @BindView(R.id.instruction_text)
    TextView mTvInstructions;

    private SimpleExoPlayer mExoPlayer;
    private Step mStep;
    private Unbinder unbinder;

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
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_POSITION)) {
            mCurrentPosition = savedInstanceState.getLong(KEY_POSITION);
            mPlayWhenReady = savedInstanceState.getBoolean(KEY_READY_TO_PLAY);
        }
        unbinder = ButterKnife.bind(this, rootView);
        mTvInstructions.setText(mStep.getDescription());
        if (!mStep.getThumbnailURL().isEmpty()) {
            GlideApp.with(this).load(mStep.getThumbnailURL()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.recipe_placeholder).into(mIvThumbnail);
            mIvThumbnail.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mStep.getVideoURL()))
            initializePlayer(Uri.parse(mStep.getVideoURL()));
        else {
            // Un- hide InstructionsContainer because in case of phone landscape is hidden
            mInstructionsContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Logger.d("onDestroyView");
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
            mExoPlayerView.setPlayer(mExoPlayer);
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
            mExoPlayerView.setVisibility(View.VISIBLE);
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
