package com.example.oud.user.player;

import com.google.android.exoplayer2.SimpleExoPlayer;

public interface PlayerInterface {


    public SimpleExoPlayer getSimpleExoPlayer();

    public void configurePlayer(String trackId,boolean resetPlay);

    public PlayerHelper getPlayerHelper();

}
