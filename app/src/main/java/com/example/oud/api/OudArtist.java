package com.example.oud.api;

import java.util.ArrayList;

public class OudArtist {

    private ArrayList<Artist> artists;

    private int limit;

    private int offset;

    private int total;

    public ArrayList<Artist> getArtists() {
        return artists;
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
