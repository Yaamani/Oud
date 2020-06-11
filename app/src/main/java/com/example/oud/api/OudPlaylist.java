package com.example.oud.api;

import java.util.ArrayList;

public class OudPlaylist {

    private ArrayList<Playlist> playlists;

    private int limit;

    private int offset;

    private int total;

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
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
