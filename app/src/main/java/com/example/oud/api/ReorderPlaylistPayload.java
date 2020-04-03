package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class ReorderPlaylistPayload {

    @SerializedName("range_start")
    private int rangeStart;

    @SerializedName("range_length")
    private int rangeLength;

    @SerializedName("insert_before")
    private int insertBefore;

    public ReorderPlaylistPayload(int rangeStart, int rangeLength, int insertBefore) {
        this.rangeStart = rangeStart;
        this.rangeLength = rangeLength;
        this.insertBefore = insertBefore;
    }

    public int getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(int rangeStart) {
        this.rangeStart = rangeStart;
    }

    public int getRangeLength() {
        return rangeLength;
    }

    public void setRangeLength(int rangeLength) {
        this.rangeLength = rangeLength;
    }

    public int getInsertBefore() {
        return insertBefore;
    }

    public void setInsertBefore(int insertBefore) {
        this.insertBefore = insertBefore;
    }

}
