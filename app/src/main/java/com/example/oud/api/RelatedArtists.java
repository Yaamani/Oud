package com.example.oud.api;

import java.util.ArrayList;

public class RelatedArtists {

    private ArrayList<Artist> artists;


    public RelatedArtists(ArrayList<Artist> artists) {
        this.artists = artists;
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }
}
