package com.example.oud.api;

public class Context {

    public static final String CONTEXT_UNKNOWN  = "unknown";
    public static final String CONTEXT_ALBUM    = "album";
    public static final String CONTEXT_ARTIST   = "artist";
    public static final String CONTEXT_PLAYLIST = "playlist";

    private String type;
    private String id;

    public Context(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }
}
