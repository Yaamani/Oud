package com.example.oud.api;

public class FollowingOrFollowersResponse {
    UserOrArtistPreview items;
    int limit;
    int offset;
    int total;

    public UserOrArtistPreview getItems() {
        return items;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotal() {
        return total;
    }

    public FollowingOrFollowersResponse(UserOrArtistPreview items, int limit, int offset, int total) {
        this.items = items;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
    }
}
