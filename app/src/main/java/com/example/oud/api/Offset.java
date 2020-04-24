package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class Offset {

    @SerializedName("position")
    Integer mPosition;

    public Offset(Integer position){

        mPosition = position;

    }


}
