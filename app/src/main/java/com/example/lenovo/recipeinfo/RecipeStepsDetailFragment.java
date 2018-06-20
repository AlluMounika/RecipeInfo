package com.example.lenovo.recipeinfo;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single RecipeSteps detail screen.
 * This fragment is either contained in a {@link RecipeStepsListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepsDetailActivity}
 * on handsets.
 */
public class RecipeStepsDetailFragment extends Fragment implements ExoPlayer.EventListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    TextView stepsdescription;
    ImageView thumbimg;
    String videoUrl;
    Uri videoURI;
    long position;
    MediaSessionCompat mediaSession;
    PlaybackStateCompat.Builder stateBuilder;
    Boolean exoplayerReady;
    long currentVideoPosition;
    public static int flag;
    public RecipeStepsDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipesteps_detail, container, false);
       thumbimg=(ImageView) rootView.findViewById(R.id.thumbnailimage);
        Bundle bundlefragment1=getArguments();
        Step steps = bundlefragment1.getParcelable("adapterBundle");
        TextView description = rootView.findViewById (R.id.descriptionFragment);
        exoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.exo_playerFragment);

        if (!(steps.getVideoURL().isEmpty())) {

            Toast.makeText(getContext(), steps.getVideoURL(), Toast.LENGTH_SHORT).show();
            videoUrl = steps.getVideoURL();
            initializePlayer();
        } else if (!(steps.getThumbnailURL().isEmpty())) {
            videoUrl = steps.getThumbnailURL();
            if(steps.getThumbnailURL().equalsIgnoreCase("mp4"))
            {
                Picasso.get().load(videoURI).into(thumbimg);
            }
            initializePlayer();

        } else {
            exoPlayerView.setVisibility(View.GONE);
            Toast.makeText(getContext(), "NO VIDEO", Toast.LENGTH_SHORT).show();
        }
        if(savedInstanceState!=null)
        {
            currentVideoPosition=savedInstanceState.getLong("current_position");
            Log.i("currentposition",Long.toString(currentVideoPosition));
            exoplayerReady=savedInstanceState.getBoolean("play_back");
        }
        try{
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector=new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer= ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector);
            Uri videoURI=Uri.parse(videoUrl);
            DefaultHttpDataSourceFactory dataSourcefactory=new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory=new DefaultExtractorsFactory();
            MediaSource mediaSource=new ExtractorMediaSource(videoURI,dataSourcefactory,extractorsFactory,null,null);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(false);
        }catch (Exception e)
        {
            Log.e("Mainactivity","error");
        }
        description.setText(steps.getDescription());

        return rootView;
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
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer();
        }
    }
    private void initializePlayer() {
        Log.i("initialize","player");
        initializeMediaSession();
    }

    private void releasePlayer() {
        Log.i("release","player");
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
        }
        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)exoPlayerView.getLayoutParams();
            params.width= Resources.getSystem().getDisplayMetrics().widthPixels;
            params.height=Resources.getSystem().getDisplayMetrics().heightPixels;
            exoPlayerView.setLayoutParams(params);
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)exoPlayerView.getLayoutParams();
            params.width= Resources.getSystem().getDisplayMetrics().widthPixels;
            params.height=600;
            exoPlayerView.setLayoutParams(params);
        }
    }

    private void initializeMediaSession() {
        Log.i("media","session");
        mediaSession = new MediaSessionCompat(getContext(), this.getClass().getSimpleName());
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                exoPlayer.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                exoPlayer.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                exoPlayer.seekTo(0);
            }
        });
        mediaSession.setActive(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                   exoPlayer.getCurrentPosition(), 1f);
        } else {
        }
        if (stateBuilder != null) {
            mediaSession.setPlaybackState(stateBuilder.build());
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("flag",flag);
        outState.putLong("current_position",exoPlayer.getCurrentPosition());
        Log.i("currentposition",Long.toString(exoPlayer.getCurrentPosition())+"posi");
       if(exoPlayer!=null){
            outState.putLong("current_position",exoPlayer.getCurrentPosition());

            outState.putBoolean("play_back",exoPlayer.getPlayWhenReady());}
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null)
        {
            flag=savedInstanceState.getInt("flag");
            currentVideoPosition=savedInstanceState.getLong("current_position");
            if(videoUrl!=null){
                exoPlayer.seekTo(currentVideoPosition);
                exoplayerReady=savedInstanceState.getBoolean("play_back");
                exoPlayer.setPlayWhenReady(exoplayerReady);
            }
        }
    }

}
