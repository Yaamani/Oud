package com.example.oud.api;

@Deprecated
public class ListOfCategories {

    private Category category;
    private int limit;
    private int offset;
    private int total;

    public ListOfCategories(Category category, int limit, int offset, int total) {
        this.category = category;
        this.limit = limit;
        this.offset = offset;
        this.total = total;
    }

    public Category getCategory() {
        return category;
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
