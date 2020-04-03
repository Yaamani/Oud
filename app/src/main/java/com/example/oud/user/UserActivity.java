package com.example.oud.user;

import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OfflineFragment;
import com.example.oud.OptionsFragment;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.example.oud.RenameFragment;
import com.example.oud.user.fragments.home.HomeFragment;
import com.example.oud.user.fragments.library.LibraryFragment;
import com.example.oud.user.fragments.playlist.PlaylistFragment;
import com.example.oud.user.fragments.playlist.PlaylistFragmentOpeningListener;
import com.example.oud.user.fragments.premium.PremiumFragment;
import com.example.oud.user.fragments.search.SearchFragment;
import com.example.oud.user.fragments.settings.SettingsFragment;
import com.example.oud.user.player.PlayerFragment;
import com.example.oud.user.player.PlayerHelper;
import com.example.oud.user.player.PlayerInterface;
import com.example.oud.user.player.smallplayer.SmallPlayerFragment;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media.session.MediaButtonReceiver;

public class UserActivity extends AppCompatActivity implements ConnectionStatusListener, ReconnectingListener, PlaylistFragmentOpeningListener , PlayerInterface  {

    private static final String TAG = UserActivity.class.getSimpleName();


    private String userId;


    private Toast mConnectionFailedToast;
    private BottomNavigationView bottomNavigationView;
    private boolean backButtonPressed = false;
    //private boolean navigationItemReselected = false;
    private Stack<Integer> bottomNavViewBackStack = new Stack<>(); // Menu Item Ids

    private  Fragment mSmallPlayerFragment;
    private NotificationManager mNotificationManager;
    private static MediaSessionCompat mMediaSession;
    private PlayerHelper mPlayerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportActionBar().hide();

        mSmallPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.container_small_player);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mPlayerHelper = new PlayerHelper(this,mNotificationManager);
        mMediaSession = PlayerHelper.getMediaSession();


        FragmentContainerView fragmentContainerView = findViewById(R.id.container_small_player);
        bottomNavigationView = findViewById(R.id.nav_view);
        fragmentContainerView.setOnClickListener(view -> {

            FragmentTransaction bigPlayer = getSupportFragmentManager().beginTransaction();
            bottomNavigationView.setVisibility(View.GONE);
            // TODO: Animate instead of just setVisibility(View.GONE)

            /*ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1000).setDuration(400);
            valueAnimator.addUpdateListener(animation -> bottomNavigationView.setY((Float) animation.getAnimatedValue()));
            valueAnimator.start();*/



            bigPlayer.replace(R.id.big_player_fragment, new PlayerFragment(), Constants.BIG_PLAYER_FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        });

        userId = (String) getIntent().getExtras().get(Constants.USER_ID_KEY);


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
        homeTransaction.replace(R.id.nav_host_fragment, new HomeFragment(), Constants.HOME_FRAGMENT_TAG);
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
    }

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

        if (f instanceof HomeFragment) return;
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
                //selected = new HomeFragment();
                HomeFragment homeFragment = (HomeFragment) manager.findFragmentByTag(Constants.HOME_FRAGMENT_TAG);
                if (homeFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new HomeFragment(), Constants.HOME_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, homeFragment, Constants.HOME_FRAGMENT_TAG);

                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/
                break;
            case R.id.navigation_search:
                //selected = new SearchFragment();
                SearchFragment searchFragment = (SearchFragment) manager.findFragmentByTag(Constants.SEARCH_FRAGMENT_TAG);
                if (searchFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new SearchFragment(), Constants.SEARCH_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, searchFragment, Constants.SEARCH_FRAGMENT_TAG);

                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/

                break;
            case R.id.navigation_library:
                //selected = new LibraryFragment();
                LibraryFragment libraryFragment = (LibraryFragment) manager.findFragmentByTag(Constants.LIBRARY_FRAGMENT_TAG);
                if (libraryFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new LibraryFragment(), Constants.LIBRARY_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment,libraryFragment, Constants.LIBRARY_FRAGMENT_TAG);

                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/
                break;
            case R.id.navigation_premium:
                //selected = new PremiumFragment();
                PremiumFragment premiumFragment = (PremiumFragment) manager.findFragmentByTag(Constants.PREMIUM_FRAGMENT_TAG);
                if (premiumFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new PremiumFragment(), Constants.PREMIUM_FRAGMENT_TAG);
                else
                    transaction.replace(R.id.nav_host_fragment, premiumFragment, Constants.PREMIUM_FRAGMENT_TAG);

                /*transaction.replace(R.id.container_small_player , smallPlayerFragment);*/
                break;
            case R.id.navigation_settings:
                //selected = new SettingsFragment();
                SettingsFragment settingsFragment = (SettingsFragment) manager.findFragmentByTag(Constants.SETTINGS_FRAGMENT_TAG);
                if (settingsFragment == null)
                    transaction.replace(R.id.nav_host_fragment, new SettingsFragment(), Constants.SETTINGS_FRAGMENT_TAG);
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
        if (f instanceof HomeFragment)
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
            offlineFragmentTransaction.commitNow();
        }

        if (mConnectionFailedToast == null) {
            mConnectionFailedToast = Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT);
        }
        mConnectionFailedToast.show();

    }

    @Override
    public void onTryingToReconnect() {

        //hideOfflineFragment();

        Fragment fragment = null;

        String[] fragmentTags = {Constants.HOME_FRAGMENT_TAG,
                Constants.LIBRARY_FRAGMENT_TAG,
                Constants.SEARCH_FRAGMENT_TAG,
                Constants.PREMIUM_FRAGMENT_TAG,
                Constants.SETTINGS_FRAGMENT_TAG};

        for (String tag : fragmentTags) {
            fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment != null) break;
        }

        if (fragment != null) {


            if (fragment instanceof ReconnectingListener) {
                ((ReconnectingListener) fragment).onTryingToReconnect();
            } else {
                throw new RuntimeException("onRetryToConnect: " + fragment.toString()
                        + " must implement " + ReconnectingListener.class.getSimpleName());
            }


        } else {
            BottomNavigationView navView = findViewById(R.id.nav_view);

            inflateFragmentBasedOnMenuItem(navView.getSelectedItemId());
        }

    }

    private void hideOfflineFragment() {
        OfflineFragment offlineFragment = (OfflineFragment) getSupportFragmentManager().findFragmentByTag(Constants.OFFLINE_FRAGMENT_TAG);
        if (offlineFragment != null) {
            FragmentTransaction offlineFragmentTransaction = getSupportFragmentManager().beginTransaction();
            offlineFragmentTransaction.remove(offlineFragment);
            offlineFragmentTransaction.commitNow();
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
     * these functions deal with track in fragments (home or search) and small player fragment
     * */
    @Override
    public SimpleExoPlayer getSimpleExoPlayer() {

       return mPlayerHelper.getExoPlayer();
    }

    @Override
    public void configurePlayer(String trackId,boolean resetPlay)  {

        mPlayerHelper.setTrackId(trackId);
        mPlayerHelper.setResetPlay(resetPlay);

        if(mSmallPlayerFragment == null) {

            FragmentTransaction smallPlayerTransaction = getSupportFragmentManager().beginTransaction();
            smallPlayerTransaction.replace(R.id.container_small_player, new SmallPlayerFragment(), Constants.SMALL_PLAYER_FRAGMENT_TAG)
                    .commit();
        }

    }

    @Override

    public PlayerHelper getPlayerHelper() {
        return mPlayerHelper;
    }

    public void createSmallFragmentForFirstTime() {

        /*Fragment smallPlayerFragment = getSupportFragmentManager().findFragmentById(R.id.container_small_player);*/

        FragmentTransaction smallPlayerTransaction = getSupportFragmentManager().beginTransaction();
        smallPlayerTransaction.replace(R.id.container_small_player , new SmallPlayerFragment(), Constants.SMALL_PLAYER_FRAGMENT_TAG)
                .commit();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayerHelper.releasePlayer();
    }

    // for handle button click in notification
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {

            MediaButtonReceiver.handleIntent(mMediaSession, intent);

        }
    }

    // handle Hand Free problem
    public class BecomingNoisyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {

                if(mPlayerHelper != null) {
                    mPlayerHelper.getExoPlayer().setPlayWhenReady(false);
                }
            }
        }
    }

    /*public interface UserActivityCommunicationListener {
        void onReconnecting();
    }*/
}
