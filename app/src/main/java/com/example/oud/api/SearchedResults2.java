package com.example.oud.api;

public class SearchedResults2 {

    private OudTrack tracks;
    private OudAlbum albums;
    private OudArtist artists;
    /*private OudList<User> users;*/
    private OudPlaylist playlists;

    public OudTrack getTracks() {
        return tracks;
    }

    public OudAlbum getAlbums() {
        return albums;
    }

    public OudArtist getArtists() {
        return artists;
    }

    public OudPlaylist getPlaylists() {
        return playlists;
    }
}
