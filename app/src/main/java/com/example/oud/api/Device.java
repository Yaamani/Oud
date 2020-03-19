package com.example.oud.api;

public class Device {
    private String id;
    private boolean isActive;
    private boolean isPrivateSession;
    private String name;
    private String type;
    private int volumePercent;

    public Device(String id, boolean isActive, boolean isPrivateSession, String name, String type, int volumePercent) {
        this.id = id;
        this.isActive = isActive;
        this.isPrivateSession = isPrivateSession;
        this.name = name;
        this.type = type;
        this.volumePercent = volumePercent;
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isPrivateSession() {
        return isPrivateSession;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getVolumePercent() {
        return volumePercent;
    }
}
