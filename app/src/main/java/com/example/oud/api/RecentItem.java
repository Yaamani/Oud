package com.example.oud.api;

import java.util.ArrayList;

public class RecentItem {

    private String _id;
    private String displayName;
    private String type;
    private ArrayList<String> images;

    public String get_id() {
        return _id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getType() {
        return type;
    }

    public ArrayList<String> getImages() {
        return images;
    }
}
