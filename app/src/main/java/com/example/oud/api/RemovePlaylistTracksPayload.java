package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;

public class RemovePlaylistTracksPayload {

    @SerializedName("tracks")
    private ArrayList<String> ids;

    public RemovePlaylistTracksPayload(String... ids) {
        //this.ids = ids;
        this.ids = new ArrayList<>();
        Collections.addAll(this.ids, ids);
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
}
