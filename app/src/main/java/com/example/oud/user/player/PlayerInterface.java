package com.example.oud.user.player;

import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;
import java.util.List;

public interface PlayerInterface {

    void configurePlayer(String contextId,String contextType ,Integer offset, String token);

    void configurePlayer(ArrayList<String> ids, String token);

    PlayerHelper getPlayerHelper();

    MediaBrowserHelper getMediaBrowserHelper();

}
