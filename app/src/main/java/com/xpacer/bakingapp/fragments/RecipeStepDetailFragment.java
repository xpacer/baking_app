package com.xpacer.bakingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.xpacer.bakingapp.R;
import com.xpacer.bakingapp.data.RecipeStep;
import com.xpacer.bakingapp.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment {

    @BindView(R.id.tv_step_description)
    TextView tvDescription;

    @BindView(R.id.tv_previous)
    TextView mPreviousBtn;

    @BindView(R.id.tv_next)
    TextView mNextBtn;

    @BindView(R.id.playerView)
    PlayerView mPlayerView;

    private SimpleExoPlayer mExoPlayer;
    private RecipeStep recipeStep;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private int mStepIndex;
    private static final DefaultBandwidthMeter BANDWIDTH_METER =
            new DefaultBandwidthMeter();
    private OnRecipeButtonsClickListener clickListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            clickListener = (OnRecipeButtonsClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeButtonsClickListener");
        }
    }

    public RecipeStepDetailFragment() {

    }

    public interface OnRecipeButtonsClickListener {
        void onRecipeStepButtonClicked(int position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recipe_step_detail, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        if (args == null ||
                !args.containsKey(Constants.ARG_RECIPE_STEP_DETAIL) ||
                !args.containsKey(Constants.ARG_RECIPE_STEP_INDEX)) {
            return view;
        }

        recipeStep = args.getParcelable(Constants.ARG_RECIPE_STEP_DETAIL);
        mStepIndex = args.getInt(Constants.ARG_RECIPE_STEP_INDEX);

        if (recipeStep != null) {
            setupView();
        }

        return view;
    }

    private void setupView() {
        tvDescription.setText(recipeStep.getDescription());
        if (recipeStep.getVideoUrl().isEmpty()) {
            mPlayerView.setVisibility(View.GONE);
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
        }

        mPreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onRecipeStepButtonClicked(mStepIndex - 1);
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onRecipeStepButtonClicked(mStepIndex + 1);
            }
        });

        initializePlayer();
    }

    private void initializePlayer() {
        if (recipeStep == null || recipeStep.getVideoUrl().isEmpty() || mExoPlayer != null) {
            return;
        }

        TrackSelection.Factory adaptiveTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(adaptiveTrackSelectionFactory),
                new DefaultLoadControl());

        mPlayerView.setPlayer(mExoPlayer);

        mExoPlayer.setPlayWhenReady(playWhenReady);
        mExoPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(recipeStep.getVideoUrl());
        MediaSource mediaSource = buildMediaSource(uri);
        mExoPlayer.prepare(mediaSource, true, false);
    }

    private MediaSource buildMediaSource(Uri uri) {
        String userAgent = Util.getUserAgent(getContext(), "bakingapp");

        DataSource.Factory manifestDataSourceFactory =
                new DefaultDataSourceFactory(getContext(), userAgent);
        return new ExtractorMediaSource.Factory(manifestDataSourceFactory).createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            playbackPosition = mExoPlayer.getCurrentPosition();
            currentWindow = mExoPlayer.getCurrentWindowIndex();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
