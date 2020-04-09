package com.example.oud.user.fragments.profile;

import androidx.lifecycle.MutableLiveData;

import com.example.oud.api.PlaylistPreview;

import java.util.List;

public class MutablePlaylistWithTotal {
    public MutablePlaylistWithTotal(MutableLiveData<List<PlaylistPreview>> playlists, MutableLiveData<Integer> total) {
        this.playlists = playlists;
        this.total = total;
    }

    MutableLiveData<List<PlaylistPreview>> playlists;
    MutableLiveData<Integer> total;

    public MutableLiveData<List<PlaylistPreview>> getPlaylists() {
        return playlists;
    }

    public MutableLiveData<Integer> getTotal() {
        return total;
    }
}
