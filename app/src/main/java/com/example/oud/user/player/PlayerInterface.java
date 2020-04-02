package com.example.oud.user.player;

import com.google.android.exoplayer2.SimpleExoPlayer;

public interface PlayerInterface {

    public SimpleExoPlayer getSimpleExoPlayer();

    public void setTrackId(String trackID);

    public boolean restAndPlay(boolean state);

    public void createSmallFragmentForFirstTime ();
}
