package com.example.oud.api;

public class Categories {

    String mCategoryIcon;
    String mCategoryName;
    String mCategoryIcon2;
    String mCategoryName2;

    public Categories(String categoryIcon, String categoryIcon2, String CategoryName, String CategoryName2){

        mCategoryIcon = categoryIcon;
        mCategoryName = CategoryName;
        mCategoryIcon2 = categoryIcon2;
        mCategoryName2 = CategoryName2;

    }

    public String getCategoryIcon() {
        return mCategoryIcon;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public String getCategoryIcon2() {
        return mCategoryIcon2;
    }

    public String getCategoryName2() {
        return mCategoryName2;
    }
}
