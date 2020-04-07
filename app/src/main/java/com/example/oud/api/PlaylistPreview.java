package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class PlaylistPreview {
    @SerializedName("_id")
    private String id;


    private String name;

    private String owner;

    private String description;

    @SerializedName("image")
    private String  imageUrl;

    private String type;

    private boolean collaborative;

    @SerializedName("public")
    private boolean isPublic;



    public PlaylistPreview(String id, String name, String owner, String description, String imageUrl, String type, boolean collaborative, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.imageUrl = imageUrl;
        this.type = type;
        this.collaborative = collaborative;
        this.isPublic = isPublic;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    public boolean isCollaborative() {
        return collaborative;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
