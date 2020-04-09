package com.example.oud.user.player.smallplayer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class SmallPlayerFragment extends ConnectionAwareFragment<SmallPlayerViewModel> {

    private View v;
    private PlayerControlView mPlayerControlView;
    private SimpleExoPlayer mExoPlayer;
    private PlayerInterface mPlayerInterface;
    private boolean restPlayer = false;
    private PlayerHelper mPlayerHelper;
    private DefaultTimeBar defaultTimeBar;
    private Track mTrack;
    private String trackId ="track";
    private boolean isLoadingTrack;
    private TextView loadingFailed;
    private Album mAlbum;

    public SmallPlayerFragment() {
        super(SmallPlayerViewModel.class, R.layout.small_player_fragment, R.id.loading_track, null);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPlayerHelper = mPlayerInterface.getPlayerHelper();

        /*trackId = mPlayerHelper.getTrackId();
        Toast.makeText(getContext(), trackId, Toast.LENGTH_SHORT).show();*/

        /*restPlayer = mPlayerHelper.isResetPlay();*/

        v = view;

        initializeViews();

        /*mViewModel.getTrackMutableLiveData(trackId).observe(getViewLifecycleOwner(), track -> {

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
        });*/

        mPlayerHelper.getPlayerback(Uri.parse(trackId));
        mExoPlayer = mPlayerHelper.getExoPlayer();
        /*defaultTimeBar.setEnabled(false);*/
        mPlayerControlView.setPlayer(mExoPlayer);
        mExoPlayer.setPlayWhenReady(true);



    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {

            mPlayerInterface = (PlayerInterface) context;

        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() +
                    " must implement methods of  PlayerInterface");
        }
    }

    private void initializeViews() {

        mPlayerControlView = v.findViewById(R.id.player_control_view);
        mPlayerControlView.setVisibility(View.VISIBLE);

        defaultTimeBar = v.findViewById(R.id.exo_progress);

        loadingFailed = v.findViewById(R.id.text_loading_failed);
        loadingFailed.setVisibility(View.GONE);
    }

    private void initializePlayerControlView() {

        TextView playerName = v.findViewById(R.id.text_player_name);
        TextView artistName = v.findViewById(R.id.text_artist_name);

        playerName.setText(mTrack.getName());
        artistName.setText(mPlayerHelper.getArtistsNames());

        mExoPlayer = mPlayerHelper.getExoPlayer();

        defaultTimeBar.setEnabled(false);

        mPlayerControlView.setPlayer(mExoPlayer);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        restPlayer = false;
    }


}
