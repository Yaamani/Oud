package com.example.oud.connectionaware;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ConnectionAwareFragment<ConnectionAwareViewM extends ConnectionAwareViewModel> extends Fragment implements ReconnectingListener, ConnectionStatusListener{

    private static final String TAG = ConnectionAwareFragment.class.getSimpleName();

    protected ConnectionAwareViewM mViewModel;
    private Class<ConnectionAwareViewM> viewModelClass;

    private ConnectionStatusListener connectionStatusListenerWhoHandlesYouAreOffline; // Most likely an activity.

    @LayoutRes
    private int layoutId;
    @IdRes
    private int progressBarId;
    @IdRes
    private Integer viewBlockUiId;
    @IdRes
    private Integer swipeRefreshLayoutId;

    private ProgressBar mProgressBar;
    private View mViewBlockUi;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private LinkedList<Toast> toasts;

    public ConnectionAwareFragment(Class<ConnectionAwareViewM> viewModelClass,
                                   @LayoutRes int layoutId,
                                   @IdRes int progressBarId,
                                   @Nullable @IdRes Integer swipeRefreshLayoutId) {
        this(viewModelClass, layoutId, progressBarId, null, swipeRefreshLayoutId);
    }

    public ConnectionAwareFragment(Class<ConnectionAwareViewM> viewModelClass,
                                   @LayoutRes int layoutId,
                                   @IdRes int progressBarId,
                                   @Nullable @IdRes Integer viewBlockUiId,
                                   @Nullable @IdRes Integer swipeRefreshLayoutId) {
        this.viewModelClass = viewModelClass;
        this.layoutId = layoutId;
        this.progressBarId = progressBarId;
        this.viewBlockUiId = viewBlockUiId;
        this.swipeRefreshLayoutId = swipeRefreshLayoutId;

        toasts = new LinkedList<>();
    }


    public ConnectionAwareFragment(Class<ConnectionAwareViewM> viewModelClass,
                                   @LayoutRes int layoutId,
                                   ProgressBar progressBar,
                                   View viewBlockUi,
                                   @Nullable @IdRes Integer swipeRefreshLayoutId) {

        this.viewModelClass = viewModelClass;
        this.layoutId = layoutId;
        this.swipeRefreshLayoutId = swipeRefreshLayoutId;

        mViewBlockUi = viewBlockUi;
        mProgressBar = progressBar;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(layoutId, container, false);


        Log.i(TAG, "onCreateView: " + "Hello from ConnectionAwareFragment.");

        if(mProgressBar == null)
            mProgressBar = root.findViewById(progressBarId);


        if (viewBlockUiId != null && mViewBlockUi==null)
            mViewBlockUi = root.findViewById(viewBlockUiId);

        if (swipeRefreshLayoutId != null) {
            mSwipeRefreshLayout = root.findViewById(swipeRefreshLayoutId);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        }

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgressBar();
    }

    /**
     * ViewModel object is available here.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(viewModelClass);
        blockUiAndWait();
        //showProgressBar();


        mViewModel.getConnectionStatus().observe(getViewLifecycleOwner(), connectionStatus -> {
            if (getContext() instanceof ConnectionStatusListener) {

                connectionStatusListenerWhoHandlesYouAreOffline = ((ConnectionStatusListener) getContext());

                if (connectionStatus == Constants.ConnectionStatus.SUCCESSFUL) {
                    onConnectionSuccess();
                } else {
                    onConnectionFailure();
                }



            } else {
                throw new RuntimeException(getContext().toString() +
                        " must implement " + ConnectionStatusListener.class.getSimpleName());
            }


            Log.i(TAG, "onViewCreated: " + connectionStatus);


            if (mProgressBar != null)
                if (mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);


            if (mSwipeRefreshLayout != null)
                mSwipeRefreshLayout.setRefreshing(false);

        });

        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                onTryingToReconnect();

                mProgressBar.setVisibility(View.GONE);
            });

    }


    protected void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Call this if you want the user to stop interacting with the app. It'll show the progress bar and the view that blocks the ui input.
     */
    public void blockUiAndWait() {
        if (mViewBlockUi != null)
            mViewBlockUi.setVisibility(View.VISIBLE);
        showProgressBar();
    }

    /**
     * Enables ui interaction. See {@link ConnectionAwareFragment#blockUiAndWait()}.
     */
    protected void unBlockUi() {
        if (mViewBlockUi != null)
            mViewBlockUi.setVisibility(View.GONE);
        hideProgressBar();
    }

    /**
     * Used for testing only
     * @return
     */
    @Deprecated
    public ConnectionAwareViewM getmViewModel() {
        return mViewModel;
    }

    @Override
    public void onConnectionSuccess() {
        connectionStatusListenerWhoHandlesYouAreOffline.onConnectionSuccess();
        unBlockUi();

    }

    @Override
    public void onConnectionFailure() {
        connectionStatusListenerWhoHandlesYouAreOffline.onConnectionFailure();
        unBlockUi();
    }

    /**
     * <p>Creates a new toast and calls {@link Toast#show()}</p>
     * <p>If the toast already exists, it doesn't create a new one. Instead it calls {@link Toast#show()} on the existing toast.</p>
     * @param text
     * @param duration {@link Toast#LENGTH_LONG} or {@link Toast#LENGTH_SHORT}
     */
    public void showToast(String text, int duration) {
        for (Toast toast : toasts) {
            String currentToastText = ((TextView)((LinearLayout)toast.getView()).getChildAt(0)).getText().toString();

            if (currentToastText.equals(text)) {
                toast.show();
                return;
            }
        }

        Toast toast = Toast.makeText(getContext(), text, duration);
        toasts.add(toast);
        toast.show();

    }

    /**
     * <p>Creates a new toast and calls {@link Toast#show()}</p>
     * <p>If the toast already exists, it doesn't create a new one. Instead it calls {@link Toast#show()} on the existing toast.</p>
     * @param stringResourceId
     * @param duration {@link Toast#LENGTH_LONG} or {@link Toast#LENGTH_SHORT}
     */
    public void showToast(@StringRes int stringResourceId, int duration) {
        String text = getResources().getString(stringResourceId);
        showToast(text, duration);
    }

    /**
     * <p>Calls {@link Toast#cancel()} on all the existing toasts.</p>
     * <p>If the toast already exists, it doesn't create a new one. Instead it calls {@link Toast#show()} on the existing toast.</p>
     * <p>If the toast doesn't exist, it creates a new toast and calls {@link Toast#show()}</p>
     * @param text
     * @param duration
     */
    public void forceToast(String text, int duration) {
        boolean alreadyExists = false;

        for (Toast toast : toasts) {
            String currentToastText = ((TextView)((LinearLayout)toast.getView()).getChildAt(0)).getText().toString();

            if (currentToastText.equals(text)) {
                alreadyExists = true;
                toast.show();
            } else toast.cancel();
        }

        if (alreadyExists) return;

        Toast toast = Toast.makeText(getContext(), text, duration);
        toasts.add(toast);
        toast.show();
    }

    /**
     * <p>Calls {@link Toast#cancel()} on all the existing toasts.</p>
     * <p>If the toast already exists, it doesn't create a new one. Instead it calls {@link Toast#show()} on the existing toast.</p>
     * <p>If the toast doesn't exist, it creates a new toast and calls {@link Toast#show()}</p>
     * @param stringResourceId
     * @param duration
     */
    public void forceToast(@StringRes int stringResourceId, int duration) {
        String text = getResources().getString(stringResourceId);
        forceToast(text, duration);
    }

    @Override
    public void onTryingToReconnect() {
        if (mProgressBar != null)
            if (mProgressBar.getVisibility() == View.GONE)
                mProgressBar.setVisibility(View.VISIBLE);

        if (mViewModel != null)
            mViewModel.clearData();
    }
}
