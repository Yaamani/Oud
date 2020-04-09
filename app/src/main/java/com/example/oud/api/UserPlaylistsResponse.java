package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserPlaylistsResponse {


    @SerializedName("items")
    private List<PlaylistPreview> playlists;

    private int limit;
    private int offset;
    private int total;


    public List<PlaylistPreview> getPlaylists() {
        return playlists;
    }

    public UserPlaylistsResponse(List<PlaylistPreview> playlists, int limit, int offset, int total) {
        this.playlists = playlists;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
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
