package com.example.oud.api;

import java.util.List;

public class QueueInfo {

    private List<Track> tracks;
    private Integer sizeOfQueue;

    public List<Track> getTracks() {
        return tracks;
    }

    public Integer getSizeOfQueue() {
        return sizeOfQueue;
    }
}
