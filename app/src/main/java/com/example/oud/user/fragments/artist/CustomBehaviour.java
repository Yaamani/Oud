package com.example.oud.user.fragments.artist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;

public class CustomBehaviour extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = CustomBehaviour.class.getSimpleName();

    public CustomBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {

        Log.i(TAG, "onStartNestedScroll: ");

        return axes == ViewCompat.SCROLL_AXIS_VERTICAL &&
                target instanceof NestedScrollView;
    }



    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {

        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);

        Log.i(TAG, "onNestedScroll: ");

        if (dyConsumed > 0) {
            //child.setY(target.getY());
            Log.i(TAG, "onNestedScroll: " + "dyConsumed = " + dyConsumed);
        }

    }
}
