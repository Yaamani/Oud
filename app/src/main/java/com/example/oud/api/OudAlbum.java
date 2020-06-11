package com.example.oud.api;

import java.util.ArrayList;

public class OudAlbum {

    private ArrayList<AlbumPreview> albums;

    private int limit;

    private int offset;

    private int total;

    public ArrayList<AlbumPreview> getAlbums() {
        return albums;
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
