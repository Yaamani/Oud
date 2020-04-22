package com.example.oud;

import android.widget.Toast;

import androidx.annotation.DrawableRes;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.oud.connectionaware.ConnectionAwareRepository.*;


public class Constants {


    public static String BASE_URL = "https://oud-zerobase.me/api/v1/";
    public static final String IMAGES_BASE_URL = "https://oud-zerobase.me/api/";
    public static final boolean MOCK = true;
    public static final String YAMANI_MOCK_BASE_URL = "http://192.168.1.3:3000";
    public static final int OK_HTTP_MOCK_WEB_SERVER_PORT = 4331;


    public static final int SENDING = 1;
    public static final int RECEIVING = 1 << 1;
    public static final int JSON_RESPONSE = 1 << 2;
    public static final int SERVER_CONNECTION_AWARE_LOG_SETTINGS = SENDING /*| RECEIVING*/ | JSON_RESPONSE/*0*/;


    public static final String USER_HOME_RECENTLY_PLAYED = "Recently played";
    public static final int USER_HOME_CATEGORIES_COUNT = 7;
    public static final int USER_HOME_HORIZONTAL_RECYCLERVIEW_ITEM_COUNT = 6;
    @DrawableRes public static final int USER_HOME_RECENTLY_PLAYED_ICON = R.drawable.ic_history2;
    @DrawableRes public static final int USER_HOME_RECENTLY_CATEGORY_ICON = R.drawable.ic_category;

    public static final int USER_ARTIST_POPULAR_SONGS_COUNT = 5;
    public static final int USER_ARTIST_ALBUMS_SINGLE_FETCH_LIMIT = 7;

    public static final int USER_LIBRARY_SINGLE_FETCH_LIMIT = 20;

    public static final String HOME_FRAGMENT_TAG = "HOME";
    public static final String SEARCH_FRAGMENT_TAG = "SEARCH";
    public static final String LIBRARY_FRAGMENT_TAG = "LIBRARY";
    public static final String PREMIUM_FRAGMENT_TAG = "PREMIUM";
    public static final String SETTINGS_FRAGMENT_TAG = "SETTINGS";
    public static final String OFFLINE_FRAGMENT_TAG = "OFFLINE";
    public static final String PLAYLIST_FRAGMENT_TAG = "PLAYLIST";
    public static final String ARTIST_FRAGMENT_TAG = "ARTIST";
    public static final String RENAME_FRAGMENT_TAG = "RENAME";
    public static final String OPTIONS_FRAGMENT_TAG = "OPTIONS";
    public static final String PROFILE_FRAGMENT_TAG = "PROFILE";
    public static final String LIBRARY_LIKED_TRACKS_FRAGMENT_TAG = "LIBRARY_LIKED_TRACKS";
    public static final String LIBRARY_PLAYLISTS_FRAGMENT_TAG = "LIBRARY_PLAYLISTS_TRACKS";
    public static final String LIBRARY_ARTISTS_FRAGMENT_TAG = "LIBRARY_ARTISTS_TRACKS";
    public static final String LIBRARY_SAVED_ALBUMS_FRAGMENT_TAG = "LIBRARY_SAVED_ALBUMS";

    public static final String ARTIST_HOME_FRAGMENT_TAG = "HOME";
    public static final String BIO_FRAGMENT_TAG = "SEARCH";
    public static final String MY_ALBUMS_FRAGMENT_TAG = "LIBRARY";
    public static final String ARTIST_SETTINGS_FRAGMENT_TAG = "SETTINGS";
    public static final String ARTIST_OFFLINE_FRAGMENT_TAG = "OFFLINE";



    public static final String SHARED_PREFERENCES_FILE_NAME = "MyPreferences";
    public static final String SHARED_PREFERENCES_TOKEN_NAME = "token";
    public static final String SHARED_PREFERENCES_USER_ID_NAME = "USER_ID";
    public static final String SHARED_PREFERENCES_IS_AUTO_PLAY_NAME = "AUTO_PLAY";
    public static final String SHARED_PREFERENCES_IS_NOTIFICATION_ALLOWED_NAME = "NOTIFICATION_ALLOWED";


    public static final String SMALL_PLAYER_FRAGMENT_TAG = "SMALL_PLAYER";
    public static final String BIG_PLAYER_FRAGMENT_TAG = "BIG_PLAYER";

    public static final String ID_KEY = "ID";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String PLAYLIST_ID_KEY = "PLAYLIST_ID";
    public static final String TRACK_ID_KEY = "TRACK_ID";
    public static final String ALBUM_ID_KEY = "ALBUM_ID";
    public static final String ARTIST_ID_KEY = "ARTIST_ID";
    public static final String PLAYLIST_FRAGMENT_TYPE_KEY = "PLAYLIST_FRAGMENT_TYPE";

    public static final String API_UNKNOWN = "unknown";
    public static final String API_ALBUM = "album";
    public static final String API_ARTIST = "artist";
    public static final String API_PLAYLIST = "playlist";
    public static final String API_USER = "user";
    public static final String API_PREMIUM = "premium";


    public static final String BUNDLE_CREATE_ALBUM_ALBUM_NAME = "album_name";




    public enum ConnectionStatus {SUCCESSFUL, FAILED}
    public enum PlaylistFragmentType {PLAYLIST, ALBUM}



    /*public OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()
            *//*.addInterceptor(chain -> {
                Request request = chain.request();
                Response response = chain.proceed(request);
                // Log.d(TAG, "pumpConfig: " + response.code());
                if (response.code() == 403)
                    Toast.makeText(this, "يا فقير.", Toast.LENGTH_SHORT).show();
                return response;
            })*//*
            .addInterceptor(new OudUtils.LoggingInterceptor())
            .build();*/


}
