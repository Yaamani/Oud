package com.example.oud.api;

public class Notification {

    private String _id; //uri

    private String image;

    private String title;


    public Notification(String _id, String image, String title, String type, String typeId) {
        this.title = title;
        this._id = _id;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getImage() {
        return image;
    }
}
