package com.example.oud.user.fragments.home.nestedrecyclerview.decorations;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpace;

    public VerticalSpaceDecoration(Resources resources, @DimenRes int horizontalSpace) {
        this.verticalSpace = (int) resources.getDimension(horizontalSpace);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect,
                               @NonNull View view,
                               @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = verticalSpace;
    }
}
