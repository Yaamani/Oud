package com.example.oud.connectionaware;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;

public class ConnectionAwareFragment<ConnectionAwareViewM extends ConnectionAwareViewModel> extends Fragment implements ReconnectingListener, ConnectionStatusListener{

    private static final String TAG = ConnectionAwareFragment.class.getSimpleName();

    protected ConnectionAwareViewM mViewModel;
    private Class<ConnectionAwareViewM> viewModelClass;

    private ConnectionStatusListener connectionStatusListenerWhoHandlesYouAreOffline; // Most likey an activity.

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
    public void onStop() {
        super.onStop();
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
        showProgressBar();


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
    protected void blockUiAndWait() {
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

    @Override
    public void onTryingToReconnect() {
        if (mProgressBar != null)
            if (mProgressBar.getVisibility() == View.GONE)
                mProgressBar.setVisibility(View.VISIBLE);

        mViewModel.clearData();
    }
}
