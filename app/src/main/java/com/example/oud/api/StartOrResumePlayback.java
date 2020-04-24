package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StartOrResumePlayback {

    @SerializedName("contextUri")
    private String mContextUri;
    private List<String> uris;
    private Offset offset;
    private Long positionMs;

    public StartOrResumePlayback(List<String> uris){

        this.uris = uris;

    }

    public StartOrResumePlayback(Long positionMs){

        this.positionMs = positionMs;
    }

    public StartOrResumePlayback(String contextUri, Integer offset){

        this.mContextUri = contextUri;
        this.offset = new Offset(offset);
    }

    public StartOrResumePlayback(String contextUri){

        this.mContextUri = contextUri;
        /*this.offset = new Offset(offset);*/
    }

    public String getPlaylistId() {
        return mContextUri;
    }

    public List<String> getUris() {
        return uris;
    }

    public Offset getOffset() {
        return offset;
    }

    public Long getPositionMs() {
        return positionMs;
    }
}
