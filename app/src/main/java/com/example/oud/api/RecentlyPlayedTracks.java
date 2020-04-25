package com.example.oud.api;

import java.util.ArrayList;

@Deprecated
public class RecentlyPlayedTracks {

    private ArrayList<RecentlyPlayedTrack> items;
    private int limit;

    /*public RecentlyPlayedTracks(ArrayList<RecentlyPlayedTrack> items, int limit) {
        this.items = items;
        this.limit = limit;
    }*/

    public ArrayList<RecentlyPlayedTrack> getItems() {
        return items;
    }

    public int getLimit() {
        return limit;
    }
}
