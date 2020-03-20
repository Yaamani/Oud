package com.example.oud.api;

import java.util.ArrayList;

public class RecentlyPlayedTracks {

    private RecentlyPlayedTrack[] items;
    private int limit;

    public RecentlyPlayedTracks(RecentlyPlayedTrack[] items, int limit) {
        this.items = items;
        this.limit = limit;
    }

    public RecentlyPlayedTrack[] getItems() {
        return items;
    }

    public int getLimit() {
        return limit;
    }
}
