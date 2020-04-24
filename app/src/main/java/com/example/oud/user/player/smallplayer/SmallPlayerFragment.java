package com.example.oud.user.player.smallplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.oud.R;
import com.example.oud.api.Album;
import com.example.oud.api.Track;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.user.UserActivity;
import com.example.oud.user.player.MediaBrowserHelper;
import com.example.oud.user.player.PlayerHelper;
import com.example.oud.user.player.PlayerInterface;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class SmallPlayerFragment extends ConnectionAwareFragment<SmallPlayerViewModel>  {

    private View v;
    private PlayerControlView mPlayerControlView;
    private SimpleExoPlayer mExoPlayer;
    private PlayerInterface mPlayerInterface;
    private boolean restPlayer = false;
    private PlayerHelper mPlayerHelper;
    private DefaultTimeBar defaultTimeBar;
    private Track mTrack;
    private String trackId = "track";
    private boolean isLoadingTrack;
    private TextView loadingFailed;
    private Album mAlbum;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private MediaMetadataCompat mediaMetadata;
    private final static String TAG = SmallPlayerFragment.class.getSimpleName();
    private Context context;
    private MediaBrowserHelper mediaBrowserHelper;

    public SmallPlayerFragment() {
        super(SmallPlayerViewModel.class, R.layout.small_player_fragment, R.id.loading_track, null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        v = view;

        initializeViews();

        Log.d(TAG,"Fragment is opened");
        mPlayerHelper = mPlayerInterface.getPlayerHelper();


        mediaBrowserHelper = mPlayerInterface.getMediaBrowserHelper();

        if(mediaBrowserHelper !=null) {
            mediaMetadata = mediaBrowserHelper.getMediaMetadata();
        }
        else{
            Log.d(TAG,"MediaBrowserHelper is null so i can't get data");
        }

        initializePlayerControlView();



        /*mPlayerHelper = mPlayerInterface.getPlayerHelper();*/

        /*trackId = mPlayerHelper.getTrackId();
        Toast.makeText(getContext(), trackId, Toast.LENGTH_SHORT).show();*/

        /*restPlayer = mPlayerHelper.isResetPlay();*/


        mViewModel.getTrackMutableLiveData(trackId).observe(getViewLifecycleOwner(), (Track track) -> {

            mTrack = track;
            if(restPlayer) {

                if(isLoadingTrack) {

                    mViewModel.getAlbumImageMutableLiveData(mTrack.getAlbumId()).observe(getViewLifecycleOwner(),
                            album -> {
                                mAlbum = album;
                                mPlayerHelper.setAlbum(mAlbum);

                                mPlayerHelper.setTrack(mTrack);
                                mPlayerHelper.initializePlayer(Uri.parse(mTrack.getAudioUrl()));
                                initializePlayerControlView();

                                mExoPlayer.seekTo(0);
                                mExoPlayer.setPlayWhenReady(true);
                            });


                }
                else{

                    mPlayerControlView.setVisibility(View.GONE);
                    loadingFailed.setVisibility(View.VISIBLE);
                }

            }
            else{
                mPlayerHelper.getPlayerback(Uri.parse(trackId));
                mExoPlayer = mPlayerHelper.getExoPlayer();
                defaultTimeBar.setEnabled(false);
                mPlayerControlView.setPlayer(mExoPlayer);
                mExoPlayer.setPlayWhenReady(true);
            }

        });

        mViewModel.checkIsLoadingTrack().observe(getViewLifecycleOwner(), aBoolean -> {
            isLoadingTrack = aBoolean;
        });

        /*mPlayerHelper.getPlayerback(Uri.parse(trackId));
        mExoPlayer = mPlayerHelper.getExoPlayer();
        *//*defaultTimeBar.setEnabled(false);*//*
        mPlayerControlView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(true);*/


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            this.context = context;
            mPlayerInterface = (PlayerInterface) context;

        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() +
                    " must implement methods of  PlayerInterface");
        }
    }

    private void initializeViews() {

        mPlayerControlView = v.findViewById(R.id.player_control_view);

        nextButton = v.findViewById(R.id.next_button);
        prevButton = v.findViewById(R.id.prev_button);

        /*mPlayerControlView.setVisibility(View.VISIBLE);*/

        defaultTimeBar = v.findViewById(R.id.exo_progress);


        /*loadingFailed = v.findViewById(R.id.text_loading_failed);
        loadingFailed.setVisibility(View.GONE);*/
    }

    private void initializePlayerControlView() {

        TextView playerName = v.findViewById(R.id.text_player_name);
        TextView artistName = v.findViewById(R.id.text_artist_name);

        if (mediaMetadata != null) {
            playerName.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            artistName.setText(mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
        } else {
            Log.d(TAG, "mediaMetadata of currentPlayback is null");
        }

        mExoPlayer = mPlayerHelper.getExoPlayer();

        if(mExoPlayer != null){

            mPlayerControlView.setPlayer(mExoPlayer);
        }
        else{
            Log.d(TAG, "mExoPlayer is null");
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mediaBrowserHelper != null){

                    mediaBrowserHelper.getTransportControls().skipToNext();

                }
                else{
                    Log.d(TAG, "mediaBrowserHelper is null so i can't skipToNext");
                }
            }
        });

        if (mPlayerHelper != null) {

            mExoPlayer = mPlayerHelper.getExoPlayer();
            mPlayerControlView.setPlayer(mExoPlayer);

        } else {
            Log.d(TAG, "playerHelper is null ");
        }

        defaultTimeBar.setEnabled(false);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        restPlayer = false;
    }

}
