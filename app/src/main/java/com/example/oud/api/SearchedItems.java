package com.example.oud.api;

public class SearchedItems {

    private String mImage;
    private String mItemName;
    private String mItemType;
    private String mId;
    private boolean isStringOnly;

    public SearchedItems(String image, String itemName, String itemType,boolean isStringOnly) {

        mImage = image;
        mItemName = itemName;
        mItemType = itemType;
        this.isStringOnly = isStringOnly;
    }

    public SearchedItems(String image, String itemName, String itemType, String id ,boolean isStringOnly) {

        mImage = image;
        mItemName = itemName;
        mItemType = itemType;
        mId = id;
        this.isStringOnly = isStringOnly;

    }

    public SearchedItems(String type,boolean isStringOnly){

        mItemType = type;
        this.isStringOnly = isStringOnly;

    }

    public String getImage() {
        return mImage;
    }

    public String getItemName() {
        return mItemName;
    }

    public String getItemType() {
        return mItemType;
    }

    public String getId() {
        return mId;
    }

    public boolean isStringOnly(){

        return isStringOnly;
    }
}
