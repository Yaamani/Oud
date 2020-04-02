package com.example.oud.user.player;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.oud.R;
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

public class SmallPlayerFragment extends Fragment {

    private View v;
    private PlayerControlView mPlayerControlView;
    private SimpleExoPlayer mExoPlayer;
    private PlayerInterface mExoPlayerListener;
    private boolean restPlayer ;
    private DefaultTimeBar defaultTimeBar ;

    public interface ExoPlayerListener{

        public SimpleExoPlayer getSimpleExoPlayer();

        public int getTrackId(int trackID);

        public boolean restAndPlay(boolean state);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.small_player_fragment, container, false);

        initializeViews();
        initializePlayerControlView();

        restPlayer = mExoPlayerListener.restAndPlay(false);

        if(restPlayer){
            playAndRest();
        }

        return v;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mExoPlayerListener = (PlayerInterface) context;

        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement SendMediaSession");
        }
    }

    private void initializeViews(){

        mPlayerControlView = v.findViewById(R.id.player_control_view);
        defaultTimeBar = v.findViewById(R.id.exo_progress);
    }

    private void initializePlayerControlView(){

        mExoPlayer = mExoPlayerListener.getSimpleExoPlayer();

        defaultTimeBar.setEnabled(false);

        mPlayerControlView.setPlayer(mExoPlayer);

    }

    private void playAndRest(){

        mExoPlayer.seekTo(0);
        mExoPlayer.setPlayWhenReady(true);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*stateOfFirstPlaying = true;*/
    }

   /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SmallPlayerViewModel.class);
        // TODO: Use the ViewModel
    }*/

}
