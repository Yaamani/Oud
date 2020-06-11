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
    public static final boolean MOCK = false;
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

    public static final String RECENT_SEARCH_FRAGMENT_TAG = "RECENT";
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
    public static final String PREMIUM_REDEEM_SUBSCRIBE_FRAGMENT_TAG = "PREMIUM_REDEEM_SUBSCRIBE";

    public static final String ARTIST_HOME_FRAGMENT_TAG = "HOME";
    public static final String BIO_FRAGMENT_TAG = "SEARCH";
    public static final String MY_ALBUMS_FRAGMENT_TAG = "LIBRARY";
    public static final String ARTIST_SETTINGS_FRAGMENT_TAG = "SETTINGS";
    public static final String ARTIST_OFFLINE_FRAGMENT_TAG = "OFFLINE";

    public static final String DOWNLOADED_TRACKS_DATABASE_NAME = "downloaded_tracks_database";

    public static final String SHARED_PREFERENCES_FILE_NAME = "MyPreferences";
    public static final String SHARED_PREFERENCES_TOKEN_NAME = "token";
    public static final String SHARED_PREFERENCES_USER_ID_NAME = "USER_ID";
    public static final String SHARED_PREFERENCES_IS_AUTO_PLAY_NAME = "AUTO_PLAY";
    public static final String SHARED_PREFERENCES_IS_NOTIFICATION_ALLOWED_NAME = "NOTIFICATION_ALLOWED";
    public static final String SHARED_PREFERENCES_USER_TYPE = "USER_TYPE"; // premium - free


    public static final String SMALL_PLAYER_FRAGMENT_TAG = "SMALL_PLAYER";
    public static final String BIG_PLAYER_FRAGMENT_TAG = "BIG_PLAYER";

    public static final String ID_KEY = "ID";
    public static final String USER_ID_KEY = "USER_ID";
    public static final String PLAYLIST_ID_KEY = "PLAYLIST_ID";
    public static final String TRACK_ID_KEY = "TRACK_ID";
    public static final String ALBUM_ID_KEY = "ALBUM_ID";
    public static final String ARTIST_ID_KEY = "ARTIST_ID";
    public static final String PLAYLIST_FRAGMENT_TYPE_KEY = "PLAYLIST_FRAGMENT_TYPE";

    public static final String URL_KEY = "URL";
    public static final String TRACK_IMAGE_KEY = "TRACK_IMAGE";
    public static final String TRACK_NAME_KEY = "TRACK_NAME";

    public static final String API_UNKNOWN = "unknown";
    public static final String API_ALBUM = "Album";
    public static final String API_ARTIST = "Artist";
    public static final String API_PLAYLIST = "Playlist";
    public static final String API_USER = "user";
    public static final String API_PREMIUM = "premium";


    public static final String BUNDLE_CREATE_ALBUM_ALBUM_NAME = "album_name";
    public static final String BUNDLE_CREATE_ALBUM_ARTIST_ID = "artist_id";
    public static final String BUNDLE_CREATE_ALBUM_GENRES_ID = "genres";
    public static final String BUNDLE_CREATE_ALBUM_IS_NEW_ALBUM_ID = "new_album";
    public static final String BUNDLE_CREATE_ALBUM_TYPE_ID = "album_type";
    public static final String BUNDLE_CREATE_ALBUM_RELEASE_DATE_ID = "release_date";
    public static final String BUNDLE_CREATE_ALBUM_ALBUM_ID_ID = "album_id";

    public static final String OFFLINE_TRACKS_DIR_NAME = "OfflineTracks";
    public static final String OFFLINE_TRACKS_EXTENSION = ".mp3";



    public enum ConnectionStatus {SUCCESSFUL, FAILED}
    public enum PlaylistFragmentType {PLAYLIST, ALBUM}
    public enum PlaybackState{PREPARING}

    public enum IntentAction{START_OR_RESUME_BROADCAST,STATE_OF_PLAYBACK,STATE_OF_PLAYBACK_COMING_FROM_EXOPLAYER}


    /**
     * For Player
     * */
    public static final String SHARED_PREFERENCES_PLAYER_FILE_NAME = "PlayerPreferences";
    public static final String NEW_USER = "newUser";
    public static final String FIRST_LAUNCH = "firstLaunch";
    /*public static final String START_OR_RESUME_BROADCAST = "StartOrResumeBroadCast";*/
    public static final String CONTEXT_URI = "contextUri";
    public static final String OFFSET = "offset";
    public static final String LIST_OF_TRACKS_URIS = "tracksIds";
    public static final String CURRENT_PLAYBACK_STATE = "currentPlaybackState";
    public static final String OPEN_BIG_PLAYER = "openBigPlayer";
    /*public static final String STATE_OF_PLAYBACK = "stateOfPlayback";*/
    public static final String CURRENT_PLAYBACK_STATE_SENDING_FROM_EXOPLAYER = "exPlayerState";
    public static final String POSITION_OF_PLAYBACK_ON_SEEK = "exPlayerState";

    public static final int STATE_PREPARING = 0;

    public enum ArrayOperation {ADD, SET}




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
