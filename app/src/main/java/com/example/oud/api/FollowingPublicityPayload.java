package com.example.oud.api;

import com.google.gson.annotations.SerializedName;

public class FollowingPublicityPayload {

    @SerializedName("public")
    private boolean _public;

    public FollowingPublicityPayload(boolean _public) {
        this._public = _public;
    }

    public boolean is_public() {
        return _public;
    }

    public void set_public(boolean _public) {
        this._public = _public;
    }
}
