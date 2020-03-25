package com.example.oud.api;

public class OudList<T> {

    private T[] items;

    private int limit;

    private int offset;

    private int total;

    public OudList(T[] items, int limit, int offset, int total) {
        this.items = items;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
    }

    public T[] getItems() {
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
