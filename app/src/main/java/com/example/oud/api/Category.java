package com.example.oud.api;

public class Category {

    private String _id;
    private String name;
    private String icon;
    private String[] playlists;

    public Category(String _id, String name, String icon, String[] playlists) {
        this._id = _id;
        this.name = name;
        this.icon = icon;
        this.playlists = playlists;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String[] getPlaylists() {
        return playlists;
    }
}
