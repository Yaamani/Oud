package com.example.oud.api;

public class Category2 {

    private String _id;
    private String name;
    private String icon;

    public Category2(String _id, String name, String icon) {
        this._id = _id;
        this.name = name;
        this.icon = icon;
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

}
