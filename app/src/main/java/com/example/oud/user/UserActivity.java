package com.example.oud.user;

import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OfflineFragment;
import com.example.oud.OptionsFragment;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.example.oud.RenameFragment;
import com.example.oud.user.fragments.artist.ArtistFragment;
import com.example.oud.user.fragments.home.HomeFragment2;
import com.example.oud.user.fragments.library.LibraryFragment;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.example.oud.user.fragments.playlist.PlaylistFragmentOpeningListener;
import com.example.oud.user.fragments.premium.AuthorizationHeaderConnection;
import com.example.oud.user.fragments.premium.PremiumFragment;
import com.example.oud.user.fragments.search.SearchFragment;
import com.example.oud.user.fragments.settings.SettingsFragment;
import com.example.oud.user.player.MediaBrowserHelper;
import com.example.oud.user.player.MediaBrowserHelperCallback;
import com.example.oud.user.player.MediaService;
import com.example.oud.user.player.PlayerFragment;
import com.example.oud.user.player.PlayerHelper;
import com.example.oud.user.player.PlayerInterface;
import com.example.oud.user.player.smallplayer.SmallPlayerFragment;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.mp4.Track;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.huxq17.download.Pump;
import com.huxq17.download.PumpFactory;
import com.huxq17.download.config.DownloadConfig;
import com.huxq17.download.core.DownloadRequest;
import com.huxq17.download.core.service.IDownloadConfigService;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media.session.MediaButtonReceiver;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UserActivity extends AppCompatActivity implements ConnectionStatusListener, ReconnectingListener, PlaylistFragmentOpeningListener, PlayerInterface, MediaBrowserHelperCallback {

/*
public class UserActivity extends AppCompatActivity implements ConnectionStatusListener, ReconnectingListener, PlaylistFragmentOpeningListener, PlayerInterface  {
*/


    private static final String TAG = UserActivity.class.getSimpleName();

    private String userId;

    public String getUserId() {
        return userId;
    }


    private Toast mConnectionFailedToast;
    private BottomNavigationView bottomNavigationView;
    private boolean backButtonPressed = false;
    //private boolean navigationItemReselected = false;
    private Stack<Integer> bottomNavViewBackStack = new Stack<>(); // Menu Item Ids

    private NotificationManager mNotificationManager;
    /*private static MediaSessionCompat mMediaSession;*/
    private PlayerHelper mPlayerHelper;
    private CurrentPlaybackStateReceiver currentPlaybackStateReceiver;

    private MediaBrowserHelper mediaBrowserHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().hide();

        mediaBrowserHelper = new MediaBrowserHelper(this, MediaService.class);

        mPlayerHelper = new PlayerHelper(this);

        // for making mediaSession control on ExoPlayer
        initCurrentPlaybackStateReceiver();


        if (mediaBrowserHelper != null/* && !mediaBrowserHelper.isConnected()*/) {

            mediaBrowserHelper.startTheService();
        }


        boolean isTherePlayback = false;

        if (isTherePlayback) {

            Fragment smallPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.container_small_player);

            if (smallPlayerFragment == null) {

                FragmentTransaction smallPlayerTransaction = getSupportFragmentManager().beginTransaction();
                smallPlayerTransaction.replace(R.id.container_small_player, new SmallPlayerFragment(),
                        Constants.SMALL_PLAYER_FRAGMENT_TAG)
                        .commit();
            }

        }

        if (getIntent() != null) {

            if (getIntent().getExtras() != null) {

                boolean openBigPlayer = getIntent().getExtras().getBoolean(Constants.OPEN_BIG_PLAYER);

                if (openBigPlayer) {

                    PlayerFragment.show(this, R.id.big_player_fragment,
                            mPlayerHelper, mediaBrowserHelper,
                            mediaBrowserHelper.getMediaMetadata());

                }

            } else {

                Log.d(TAG, "bundle that is coming from notification is null");
            }

        } else {
            Log.d(TAG, "intent that is coming from notification is null");
        }

        /*boolean isNewUser = OudUtils.isNewUser(getApplicationContext());

        if(!isNewUser){

            FragmentTransaction smallPlayerTransaction = getSupportFragmentManager().beginTransaction();
            smallPlayerTransaction.replace(R.id.container_small_player, new SmallPlayerFragment(),
                    Constants.SMALL_PLAYER_FRAGMENT_TAG)
                    .commit();

        }*/

        FragmentContainerView fragmentContainerView = findViewById(R.id.container_small_player);
        bottomNavigationView = findViewById(R.id.nav_view);

        fragmentContainerView.setOnClickListener(view -> {


            bottomNavigationView.setVisibility(View.GONE);
            // TODO: Animate instead of just setVisibility(View.GONE)

            /*ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1000).setDuration(400);
            valueAnimator.addUpdateListener(animation -> bottomNavigationView.setY((Float) animation.getAnimatedValue()));
            valueAnimator.start();*/
            PlayerFragment.show(this, R.id.big_player_fragment,
                    mPlayerHelper, mediaBrowserHelper,
                    mediaBrowserHelper.getMediaMetadata());
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
            userId = (String) intent.getExtras().get(Constants.USER_ID_KEY);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        navView.setOnNavigationItemSelectedListener(item -> {

            if (OptionsFragment.doesOptionsFragmentExist(this, R.id.container_options)) {
                OptionsFragment.hideOptionsFragment(this, R.id.container_options);
            }

            if (backButtonPressed)
                return true;

            /*if (navigationItemReselected) {
                navigationItemReselected = false;
                return true;
            }*/

            return UserActivity.this.inflateFragmentBasedOnMenuItem(item.getItemId());
        });

        navView.setOnNavigationItemReselectedListener(item -> {
            Log.i(TAG, "onNavigationItemReselected: ");
            //navigationItemReselected = true;

            if (OptionsFragment.doesOptionsFragmentExist(this, R.id.container_options)) {
                OptionsFragment.hideOptionsFragment(this, R.id.container_options);
            }

            if (backButtonPressed) return;

            handleBottomNavViewItemReselected();
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            Log.i(TAG, "Back stack : " + "Changed.");
            handleBottomNavViewBackStack(navView);
        });

        FragmentTransaction homeTransaction = getSupportFragmentManager().beginTransaction();
        homeTransaction.replace(R.id.nav_host_fragment, HomeFragment2.newInstance(userId), Constants.HOME_FRAGMENT_TAG);
        //homeTransaction.addToBackStack(null);
        bottomNavViewBackStack.push(R.id.navigation_home);
        homeTransaction.commit();

        //navView.setSelectedItemId(R.id.navigation_search);

        /*FragmentContainerView container = findViewById(R.id.nav_host_fragment);
        Log.i(TAG, "onCreate: " + getSupportFragmentManager().findFragmentByTag("Offline"));
        FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
        //FragmentContainerView offlineFragmentContainer = findViewById(R.id.fragment_offline_container);
        offlineFragmentTransaction.replace(R.id.fragment_offline_container, new OfflineFragment(), "Offline");
        offlineFragmentTransaction.commit();
        Log.i(TAG, "onCreate: " + getSupportFragmentManager().findFragmentByTag("Offline"));*/

        /*FragmentContainerView container = findViewById(R.id.nav_host_fragment);

        //ObjectAnimator.ofInt(container, View.MA);


        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams)(container.getLayoutParams());
        ValueAnimator marginAnimation = ValueAnimator.ofInt(params.topMargin, 150).setDuration(300);
        marginAnimation.addUpdateListener(animation -> params.setMargins(0, (int) animation.getAnimatedValue(), 0, 0));

        Button animateBtn = findViewById(R.id.btn_animate_test);
        animateBtn.setOnClickListener(v -> {
            marginAnimation.start();
            container.setLayoutParams(params);
        });*/

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ObjectAnimator.ofFloat(findViewById(R.id.nav_view), View.ALPHA, 0, 1)
                    .setDuration(700)
                    .start();
        }*/



        /*final AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_search,
                R.id.navigation_library,
                R.id.navigation_premium,
                R.id.navigation_settings).build();*/


        //appBarConfiguration.getDrawerLayout().

        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        /*NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/

        pumpConfig();

    }

    /*private boolean artistFragPaused = false;
    private String artistFragPausedArtistId;

    public void setArtistFragPaused(boolean artistFragPaused) {
        this.artistFragPaused = artistFragPaused;
    }

    public void setArtistFragPausedArtistId(String id) {
        artistFragPausedArtistId = id;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        // Motion layout bug fix.

        if (artistFragPaused) {

            String artistId = artistFragPausedArtistId;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, ArtistFragment.newInstance(artistId), Constants.ARTIST_FRAGMENT_TAG)
                    .commit();
        }

    }*/


    private void handleBottomNavViewBackStack(BottomNavigationView navView) {
        if (backButtonPressed & !bottomNavViewBackStack.isEmpty()) { // pop & peak

            bottomNavViewBackStack.pop();

            if (!bottomNavViewBackStack.isEmpty()) {
                int itemId = bottomNavViewBackStack.peek();
                navView.setSelectedItemId(itemId);
            }

        } else  // push
            bottomNavViewBackStack.push(navView.getSelectedItemId());

        backButtonPressed = false;
    }

    private void handleBottomNavViewItemReselected() {

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (f instanceof HomeFragment2) return;
        else if (f instanceof SearchFragment) return;
        else if (f instanceof LibraryFragment) return;
        else if (f instanceof PremiumFragment) return;
        else if (f instanceof SettingsFragment) return;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        inflateFragmentBasedOnMenuItem(navView.getSelectedItemId());
    }

    private boolean inflateFragmentBasedOnMenuItem(int itemId) {
        //Fragment selected = null;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();


        switch (itemId) {
            case R.id.navigation_home:
                //selected = new HomeFragment2();
                HomeFragment2 homeFragment = (HomeFragment2) manager.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
                if (homeFragment == null)
                    transaction.replace(R.id.nav_host_fragment, HomeFragment2.newInstance(userId), Constants.HOME_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, homeFragment, Constants.HOME_FRAGMENT_TAG);

                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/
                break;
            case R.id.navigation_search:
                //selected = new SearchFragment();
                SearchFragment searchFragment = (SearchFragment) manager.findFragmentByTag(Constants.SEARCH_FRAGMENT_TAG);
                if (searchFragment == null)
                    transaction.replace(R.id.nav_host_fragment, SearchFragment.newInstance(userId), Constants.SEARCH_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, searchFragment, Constants.SEARCH_FRAGMENT_TAG);

                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/

                break;
            case R.id.navigation_library:
                //selected = new LibraryFragment();
                /*LibraryFragment libraryFragment = (LibraryFragment) manager.findFragmentByTag(Constants.LIBRARY_FRAGMENT_TAG);
                if (libraryFragment == null)*/
                    transaction.replace(R.id.nav_host_fragment, new LibraryFragment(), Constants.LIBRARY_FRAGMENT_TAG);

                /*else
                    transaction.replace(R.id.nav_host_fragment, libraryFragment, Constants.LIBRARY_FRAGMENT_TAG);*/

                /*else
                    transaction.replace(R.id.nav_host_fragment,libraryFragment, Constants.LIBRARY_FRAGMENT_TAG);*/


                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/
                break;
            case R.id.navigation_premium:
                //selected = new PremiumFragment();
               /* PremiumFragment premiumFragment = (PremiumFragment) manager.findFragmentByTag(Constants.PREMIUM_FRAGMENT_TAG);
                if (premiumFragment == null)*/
                    transaction.replace(R.id.nav_host_fragment, new PremiumFragment(), Constants.PREMIUM_FRAGMENT_TAG);
                /*else
                    transaction.replace(R.id.nav_host_fragment, premiumFragment, Constants.PREMIUM_FRAGMENT_TAG);*/

                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/
                break;
            case R.id.navigation_settings:
                //selected = new SettingsFragment();
                SettingsFragment settingsFragment = (SettingsFragment) manager.findFragmentByTag(Constants.SETTINGS_FRAGMENT_TAG);
                if (settingsFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new SettingsFragment(this), Constants.SETTINGS_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, settingsFragment, Constants.SETTINGS_FRAGMENT_TAG);

                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/
                break;
        }


        //transaction.setCustomAnimations(R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();


        return true;
    }

    @Override
    public void onBackPressed() {

        Log.i(TAG, "Back stack : " + "Back button pressed.");

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        Log.i(TAG, "onBackPressed: " + fragments.get(fragments.size() - 1));


        if (RenameFragment.doesRenameFragmentExist(this, R.id.nav_host_fragment)) {
            RenameFragment.hideRenameFragment(this, R.id.nav_host_fragment);
            return;
        }

        if (OptionsFragment.doesOptionsFragmentExist(this, R.id.container_options)) {
            OptionsFragment.hideOptionsFragment(this, R.id.container_options);
            return;
        }


        bottomNavigationView.setVisibility(View.VISIBLE);


        backButtonPressed = true;

        super.onBackPressed();






        /*BottomNavigationView navView = findViewById(R.id.nav_view);

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (f instanceof HomeFragment2)
            navView.getMenu().getItem(0).setChecked(true);
        else if (f instanceof SearchFragment)
            navView.getMenu().getItem(1).setChecked(true);
        else if (f instanceof LibraryFragment)
            navView.getMenu().getItem(2).setChecked(true);
        else if (f instanceof PremiumFragment)
            navView.getMenu().getItem(3).setChecked(true);
        else if (f instanceof SettingsFragment)
            navView.getMenu().getItem(4).setChecked(true);*/

        //navView.setAlpha(0.5f);

        /*findViewById(R.id.nav_view).animate().alphaBy(.5f).setDuration(700).start();*/
        /*ValueAnimator valueAnimator = ValueAnimator.ofFloat(-getResources().getDimension(R.dimen.offline_layout_size), 0).setDuration(700);
        valueAnimator.addUpdateListener(animation -> findViewById(R.id.fragment_offline).setY((Float) animation.getAnimatedValue()));
        valueAnimator.start();*/
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged: ");
    }

    @Override
    public void onConnectionSuccess() {
        hideOfflineFragment();

        if (mConnectionFailedToast != null)
            mConnectionFailedToast.cancel();
    }

    @Override
    public void onConnectionFailure() {
        if (getSupportFragmentManager().findFragmentByTag(Constants.OFFLINE_FRAGMENT_TAG) == null) {
            FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
            offlineFragmentTransaction.replace(R.id.fragment_offline_container, new OfflineFragment(), Constants.OFFLINE_FRAGMENT_TAG);
            offlineFragmentTransaction.commit();
        }

        if (mConnectionFailedToast == null) {
            mConnectionFailedToast = Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT);
        }
        mConnectionFailedToast.show();

    }

    @Override
    public void onTryingToReconnect() {



        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            //Fragment fragment = fragments.get(fragments.size() - 2);
            //Fragment fragment = null;



            /*String[] fragmentTags = {Constants.HOME_FRAGMENT_TAG,
                    Constants.SEARCH_FRAGMENT_TAG,
                    Constants.LIBRARY_FRAGMENT_TAG,
                    Constants.PREMIUM_FRAGMENT_TAG,
                    Constants.SETTINGS_FRAGMENT_TAG};*/

            /*for (String tag : fragmentTags) {
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment != null) break;
            }

                Log.i(TAG, "onTryingToReconnect: " + fragment);

                if (fragment != null) {*/

                    /*for (Class fragClass : fragmentClasses) {
                        try {
                            ((fragClass) fragment)
                        }
                    }*/

            if (fragment instanceof ReconnectingListener) {
                ((ReconnectingListener) fragment).onTryingToReconnect();
                Log.i(TAG, "onTryingToReconnect: " + fragment);
                //break;
            } /*else {
                        //continue;
                        throw new RuntimeException("onRetryToConnect: " + fragment.getClass().getSimpleName() + " must implement " + ReconnectingListener.class.getSimpleName());
                    }*/


                /*} else {
                    BottomNavigationView navView = findViewById(R.id.nav_view);

                    inflateFragmentBasedOnMenuItem(navView.getSelectedItemId());
                }*/
        }


    }

    private void hideOfflineFragment() {
        OfflineFragment offlineFragment = (OfflineFragment) getSupportFragmentManager().findFragmentByTag(Constants.OFFLINE_FRAGMENT_TAG);
        if (offlineFragment != null) {
            FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
            offlineFragmentTransaction.remove(offlineFragment);
            offlineFragmentTransaction.commit();
        }
    }

    @Override
    public void onOpeningPlaylistFragment(Constants.PlaylistFragmentType type, String playlistOrAlbumId) {
        FragmentManager manager = getSupportFragmentManager();
        PlaylistFragment playlistFragment = (PlaylistFragment) manager.findFragmentByTag(Constants.PLAYLIST_FRAGMENT_TAG);
        if (playlistFragment == null)
            playlistFragment = PlaylistFragment.newInstance(userId, type, playlistOrAlbumId);
        else {
            playlistFragment.setArguments(PlaylistFragment.myArgs(userId, type, playlistOrAlbumId));
        }


        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.nav_host_fragment, playlistFragment, Constants.PLAYLIST_FRAGMENT_TAG);
        transaction.addToBackStack(null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }


    /**
     * Configure the download manager (Pump).
     */
    private void pumpConfig() {
        String token = OudUtils.getToken(this);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    // Log.d(TAG, "pumpConfig: " + response.code());
                    if (response.code() == 403)
                        Toast.makeText(this, "يا فقير.", Toast.LENGTH_SHORT).show();
                    return response;
                })
                .addInterceptor(new OudUtils.LoggingInterceptor())
                .build();

        if (PumpFactory.getService(IDownloadConfigService.class) != null)
            DownloadConfig.newBuilder()
                    .setDownloadConnectionFactory(new AuthorizationHeaderConnection.Factory(OudUtils.getIgnoreCertificateOkHttpClient(), token))
                    .build();
    }


    /**
     * these functions deal with track in fragments (home or search) and small player fragment
     */

    @Override
    public void configurePlayer(String contextId, String contextType, Integer offset, String token) {


        Intent intent = new Intent();
        Bundle bundle = new Bundle();


        String contextUri = "oud:"+ /*contextType.toLowerCase()*/"playlist" + ":" + "5e6dea511e17a305285ba616"/*contextId*/;

        bundle.putString(Constants.CONTEXT_URI, contextUri);
        bundle.putString(Constants.SHARED_PREFERENCES_TOKEN_NAME, token);
        bundle.putInt(Constants.OFFSET, offset);
        bundle.putStringArrayList(Constants.LIST_OF_TRACKS_URIS, null);

        intent.putExtras(bundle);
        intent.setAction(String.valueOf(Constants.IntentAction.START_OR_RESUME_BROADCAST));
        sendBroadcast(intent);

        /*mediaBrowserHelper.getTransportControls().prepare();*/

        Fragment smallPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.container_small_player);

        if (smallPlayerFragment == null) {

            FragmentTransaction smallPlayerTransaction = getSupportFragmentManager().beginTransaction();
            smallPlayerTransaction.replace(R.id.container_small_player, new SmallPlayerFragment(), Constants.SMALL_PLAYER_FRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public void configurePlayer(ArrayList<String> ids, String token) {

        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        ArrayList<String> uris = new ArrayList<>();

        for(int i=0 ;i < ids.size(); i++){

            uris.set(i,"oud:track:"+ids.get(i));
        }

        bundle.putStringArrayList(Constants.LIST_OF_TRACKS_URIS, uris);
        bundle.putString(Constants.SHARED_PREFERENCES_TOKEN_NAME, token);
        bundle.putString(Constants.CONTEXT_URI, null);
        bundle.putInt(Constants.OFFSET, -1);

        intent.putExtras(bundle);

        sendBroadcast(intent);


    }

    @Override
    public PlayerHelper getPlayerHelper() {
        return mPlayerHelper;
    }

    @Override
    public MediaBrowserHelper getMediaBrowserHelper() {

        return mediaBrowserHelper;
    }

    public void createSmallFragmentForFirstTime() {

        /*Fragment smallPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.container_small_player);*/

        FragmentTransaction smallPlayerTransaction = getSupportFragmentManager().beginTransaction();
        smallPlayerTransaction.replace(R.id.container_small_player, new SmallPlayerFragment(), Constants.SMALL_PLAYER_FRAGMENT_TAG)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (mediaBrowserHelper != null ) {
            mediaBrowserHelper.stopTheService();
            Log.d(TAG, "Stop THE SERVICE");
        }

        if (currentPlaybackStateReceiver != null) {

            unregisterReceiver(currentPlaybackStateReceiver);
        }


        mPlayerHelper.releasePlayer();
        Pump.shutdown();

    }

    /**
     * USED FOR TESTS ONLY.
     *
     * @param userId
     */
    @Deprecated
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public void mediaMetaDataChanged(MediaMetadataCompat mediaMetaData) {

    }

    /*@Override
    public void mediaMetaDataChanged(MediaMetadataCompat mediaMetaData) {

    }*/

    public class CurrentPlaybackStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {

                if (intent.getExtras() != null) {

                    int state = intent.getExtras().getInt(Constants.CURRENT_PLAYBACK_STATE);

                    switch (state) {

                        case PlaybackStateCompat.STATE_PLAYING:
                            mPlayerHelper.playCurrentPlayback();
                            break;
                        case PlaybackStateCompat.STATE_PAUSED:
                            mPlayerHelper.pauseCurrentPlayback();
                            break;
                        case PlaybackStateCompat.STATE_STOPPED:
                            mPlayerHelper.releasePlayer();
                            break;
                        case Constants.STATE_PREPARING:
                            mPlayerHelper.preparePlayback(mediaBrowserHelper.getMediaMetadata());
                            break;
                        case PlaybackStateCompat.STATE_SKIPPING_TO_PREVIOUS:
                            mPlayerHelper.resetPlayback();
                    }


                } else {

                    Log.d(TAG, "bundle of currentPlaybackState is null");
                }
            } else {

                Log.d(TAG, "intent of currentPlaybackState is null");
            }


        }
    }

    private void initCurrentPlaybackStateReceiver() {

        IntentFilter intentFilter = new IntentFilter(String.valueOf(Constants.IntentAction.STATE_OF_PLAYBACK));
        currentPlaybackStateReceiver = new CurrentPlaybackStateReceiver();
        registerReceiver(currentPlaybackStateReceiver, intentFilter);

    }

    // handle Hand Free problem


    /*public interface UserActivityCommunicationListener {
        void onReconnecting();
    }*/
}
