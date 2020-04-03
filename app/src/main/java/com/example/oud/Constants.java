package com.example.oud;

import androidx.annotation.DrawableRes;

public class Constants {

    public static String BASE_URL = "http://example.com";
    public static final boolean MOCK = true;
    public static final String YAMANI_MOCK_BASE_URL = "http://192.168.1.3:3000";

    public static final int USER_HOME_CATEGORIES_COUNT = 7;
    public static final int USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT = 6;
    @DrawableRes public static final int USER_HOME_RECENTLY_PLAYED_ICON = R.drawable.ic_history2;
    @DrawableRes public static final int USER_HOME_RECENTLY_CATEGORY_ICON = R.drawable.ic_category;

    public static final String HOME_FRAGMENT_TAG = "HOME";
    public static final String SEARCH_FRAGMENT_TAG = "SEARCH";
    public static final String LIBRARY_FRAGMENT_TAG = "LIBRARY";
    public static final String PREMIUM_FRAGMENT_TAG = "PREMIUM";
    public static final String SETTINGS_FRAGMENT_TAG = "SETTINGS";
    public static final String OFFLINE_FRAGMENT_TAG = "OFFLINE";
    public static final String PLAYLIST_FRAGMENT_TAG = "PLAYLIST";
    public static final String RENAME_FRAGMENT_TAG = "RENAME_FRAGMENT";
    public static final String OPTIONS_FRAGMENT_TAG = "OPTIONS_FRAGMENT";

    public static final String SMALL_PLAYER_FRAGMENT_TAG = "SMALL_PLAYER";
    public static final String BIG_PLAYER_FRAGMENT_TAG = "BIG_PLAYER";

    public static final String ID_KEY = "ID";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String PLAYLIST_ID_KEY = "PLAYLIST_ID";
    public static final String TRACK_ID_KEY = "TRACK_ID";
    public static final String ALBUM_ID_KEY = "ALBUM_ID";
    public static final String PLAYLIST_FRAGMENT_TYPE_KEY = "PLAYLIST_FRAGMENT_TYPE";

    public enum ConnectionStatus {SUCCESSFUL, FAILED}
    public enum PlaylistFragmentType {PLAYLIST, ALBUM}


}
