package com.example.oud;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineFragment extends Fragment {

    private static final String TAG = OfflineFragment.class.getSimpleName();

    private ReconnectingListener listener;

    public OfflineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_offline, container, false);

        Log.i(TAG, "onCreateView: " + container);

        /*ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) container.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            //params.setMargins(10, -100, 0, 0);
            //params.
        }*/

        //container.setTranslationY(-30);

        /*ValueAnimator anim = ValueAnimator.ofFloat(initialVal, finalVal).setDuration(400);
        anim.addUpdateListener(animator -> {
            *//*container.getLa*//*
        });*/

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(-getResources().getDimension(R.dimen.offline_layout_size), 0).setDuration(400);
        //valueAnimator.setStartDelay(2000);
        valueAnimator.addUpdateListener(animation -> container.setY((Float) animation.getAnimatedValue()));
        valueAnimator.start();

        v.findViewById(R.id.btn_retry_to_connect).setOnClickListener(view -> listener.onTryingToReconnect());

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ReconnectingListener) {
            listener = ((ReconnectingListener) context);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement " + ReconnectingListener.class.getSimpleName());
        }
    }

    /*public interface OfflineFragmentCommunicationListener {
        void onRetryToConnect();
    }*/
}
