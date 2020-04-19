package com.example.oud.api;

public class Context {

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
