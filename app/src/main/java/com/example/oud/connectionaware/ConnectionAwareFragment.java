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
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.ReconnectingListener;
import com.example.oud.user.fragments.playlist.PlaylistViewModel;

import java.lang.reflect.ParameterizedType;

public class ConnectionAwareFragment<ConnectionAwareViewM extends ConnectionAwareViewModel> extends Fragment implements ReconnectingListener {

    private static final String TAG = ConnectionAwareFragment.class.getSimpleName();

    protected ConnectionAwareViewM mViewModel;
    private Class<ConnectionAwareViewM> viewModelClass;

    @LayoutRes
    private int layoutId;
    @IdRes
    private int progressBarId;
    @IdRes
    private Integer swipeRefreshLayoutId;

    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ConnectionAwareFragment(Class<ConnectionAwareViewM> viewModelClass,
                                   @LayoutRes int layoutId,
                                   @IdRes int progressBarId,
                                   @Nullable @IdRes Integer swipeRefreshLayoutId) {
        this.viewModelClass = viewModelClass;
        this.layoutId = layoutId;
        this.progressBarId = progressBarId;
        this.swipeRefreshLayoutId = swipeRefreshLayoutId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(layoutId, container, false);


        Log.i(TAG, "onCreateView: " + "Hello from ConnectionAwareFragment.");


        progressBar = root.findViewById(progressBarId);

        if (swipeRefreshLayoutId != null)
            swipeRefreshLayout = root.findViewById(swipeRefreshLayoutId);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(viewModelClass);


        mViewModel.getConnectionStatus().observe(getViewLifecycleOwner(), connectionStatus -> {
            if (getContext() instanceof ConnectionStatusListener) {

                ConnectionStatusListener connectionStatusListener = ((ConnectionStatusListener) getContext());

                if (connectionStatus == Constants.ConnectionStatus.SUCCESSFUL)
                    connectionStatusListener.onConnectionSuccess();
                else
                    connectionStatusListener.onConnectionFailure();



            } else {
                throw new RuntimeException(getContext().toString() +
                        " must implement " + ConnectionStatusListener.class.getSimpleName());
            }




            if (progressBar != null)
                if (progressBar.getVisibility() == View.VISIBLE)
                    progressBar.setVisibility(View.GONE);


            if (swipeRefreshLayout != null)
                swipeRefreshLayout.setRefreshing(false);

        });

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setOnRefreshListener(() -> {
                onTryingToReconnect();

                progressBar.setVisibility(View.GONE);
            });

    }


    @Override
    public void onTryingToReconnect() {
        if (progressBar != null)
            if (progressBar.getVisibility() == View.GONE)
                progressBar.setVisibility(View.VISIBLE);

        mViewModel.clearData();
    }
}
