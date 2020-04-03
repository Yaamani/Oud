package com.example.oud.api;

import java.util.ArrayList;

public class OudList<T> {

    private ArrayList<T> items;

    private int limit;

    private int offset;

    private int total;

    public OudList(ArrayList<T> items, int limit, int offset, int total) {
        this.items = items;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
    }

    public ArrayList<T> getItems() {
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
}
