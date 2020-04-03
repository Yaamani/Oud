package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class UserPlaylistsResponse {
    @SerializedName("items")
    private PlaylistPreview[] playlists;

    private int limit;
    private int offset;
    private int total;

    public UserPlaylistsResponse(PlaylistPreview[] playlists, int limit, int offset, int total) {
        this.playlists = playlists;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
    }

    public PlaylistPreview[] getPlaylists() {
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
