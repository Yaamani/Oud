package com.example.oud.user.player;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;
import java.util.List;

public interface PlayerInterface {

    public void configurePlayer(String contextId,String contextType ,Integer offset, String token);

    public void configurePlayer(ArrayList<String> ids, String token);

    public PlayerHelper getPlayerHelper();

    public MediaBrowserHelper getMediaBrowserHelper();

}
