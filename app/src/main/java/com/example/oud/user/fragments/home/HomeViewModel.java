package com.example.oud.user.fragments.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    //private HomeRepository homeRepository;

    private OuterItemLiveData recentlyPlayedLiveData;
    private OuterItemLiveData[] categoriesLiveData;

    public HomeViewModel() {

    }

    public OuterItemLiveData getRecentlyPlayedLiveData() {
        if (recentlyPlayedLiveData == null)
            //recentlyPlayedLiveData = homeRepository.loadRecentlyPlayed();
            recentlyPlayedLiveData = loadDummy("Recently played");

        return recentlyPlayedLiveData;
    }

    public OuterItemLiveData getCategoryLiveData(int position) {
        if (categoriesLiveData == null)
            categoriesLiveData = new OuterItemLiveData[HomeFragment.CATEGORIES_COUNT];

        if (categoriesLiveData[position] == null)
            categoriesLiveData[position] = loadDummy("Category " + position);

        return categoriesLiveData[position];
    }

    private OuterItemLiveData loadDummy(String dummyTitle) {
        MutableLiveData<String> icon = new MutableLiveData<>(
                "https://i.redd.it/qn7f9oqu7o501.jpg");
        MutableLiveData<String> title = new MutableLiveData<>(dummyTitle);
        InnerItemLiveData[] innerItems = new InnerItemLiveData[HomeFragment.HORIZONTAL_RECYCLERVIEW_ITEM_COUNT];

        for (int i = 0; i < innerItems.length; i++) {
            innerItems[i] = new InnerItemLiveData();
            innerItems[i].mImage = new MutableLiveData<>(
                    "https://i.redd.it/glin0nwndo501.jpg");
            innerItems[i].mTitle = new MutableLiveData<>("Title " + i);
            innerItems[i].mSubTitle = new MutableLiveData<>("Sub title " + i);
        }

        return new OuterItemLiveData(icon, title, innerItems);
    }

    public static class OuterItemLiveData {
        private MutableLiveData<String> mIcon;
        private MutableLiveData<String> mTitle;
        private InnerItemLiveData[] mInnerItems;

        public OuterItemLiveData(MutableLiveData<String> mIcon,
                                 MutableLiveData<String> mTitle,
                                 InnerItemLiveData[] mInnerItems) {
            this.mIcon = mIcon;
            this.mTitle = mTitle;
            this.mInnerItems = mInnerItems;
        }

        public MutableLiveData<String> getIcon() {
            return mIcon;
        }

        public MutableLiveData<String> getTitle() {
            return mTitle;
        }

        public InnerItemLiveData[] getInnerItems() {
            return mInnerItems;
        }
    }

    public static class InnerItemLiveData {
        private MutableLiveData<String> mImage;
        private MutableLiveData<String> mTitle;
        private MutableLiveData<String> mSubTitle;

        public InnerItemLiveData() {

        }

        public MutableLiveData<String> getImage() {
            return mImage;
        }

        public MutableLiveData<String> getTitle() {
            return mTitle;
        }

        public MutableLiveData<String> getSubTitle() {
            return mSubTitle;
        }
    }

}