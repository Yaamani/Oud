package com.example.oud.api;

import java.util.ArrayList;

public class OudTrack {

    private ArrayList<Track> tracks;

    private int limit;

    private int offset;

    private int total;

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotal() {
        return total;
    }

}
