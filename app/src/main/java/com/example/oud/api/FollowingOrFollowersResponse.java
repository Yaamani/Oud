package com.example.oud.api;

import java.util.List;

public class FollowingOrFollowersResponse {
    List<UserOrArtistPreview> items;
    int limit;
    int offset;
    int total;


    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotal() {
        return total;
    }

    public List<UserOrArtistPreview> getItems() {
        return items;
    }

    public FollowingOrFollowersResponse(List<UserOrArtistPreview> items, int limit, int offset, int total) {
        this.items = items;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
    }
}
