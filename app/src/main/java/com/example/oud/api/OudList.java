package com.example.oud.api;

import java.util.ArrayList;

public class OudList<T> {

    private ArrayList<T> items;

    private int limit;

    private int offset;

    private int total;

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public OudList(ArrayList<T> items, int limit, int offset, int total) {
        this.items = items;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
    }

    public ArrayList<T> getItems() {
        return items;
    }

    public void addItems (ArrayList<T> items1){
        for(int i = 0;i<items1.size();i++)
            items.add(items1.get(i));
    }

    public void deleteItem (int position){
        items.remove(position);
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
