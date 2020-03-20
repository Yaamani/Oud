package com.example.oud.nestedrecyclerview.decorations;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.util.LayoutDirection;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalSpaceDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpace;
    private int itemCount;

    public HorizontalSpaceDecoration(Resources resources, @DimenRes int horizontalSpace, int itemCount) {
        this.horizontalSpace = (int) resources.getDimension(horizontalSpace);
        this.itemCount = itemCount;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

        Configuration config = view.getContext().getResources().getConfiguration();

        if (config.getLayoutDirection() == LayoutDirection.LTR) {
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.left = horizontalSpace;
        } else {
            if (parent.getChildAdapterPosition(view) == itemCount - 1)
                outRect.left = horizontalSpace;
        }
    }
}
