package com.example.oud.api;

import java.util.Date;

public class RecentlyPlayedTrack {

    private Track track;
    private Date playedAt;
    private Context context;

    /*public RecentlyPlayedTrack(Track track, Date playedAt, Context context) {
        this.track = track;
        this.playedAt = playedAt;
        this.context = context;
    }*/

    public Track getTrack() {
        return track;
    }

    public Date getPlayedAt() {
        return playedAt;
    }

    public Context getContext() {
        return context;
    }
}
