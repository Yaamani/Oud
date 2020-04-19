package com.example.oud;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.LayoutRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadMoreAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    private static final String TAG = LoadMoreAdapter.class.getSimpleName();

    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;


    private RecyclerView recyclerView;


    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    private RecyclerView.Adapter adapter;
    @LayoutRes int progressBarLayout;
    private Class<?>viewHolderClass;
    private ArrayList mainList;


    public LoadMoreAdapter(RecyclerView recyclerView,
                           RecyclerView.Adapter adapter,
                           Class<?> viewHolderClass,
                           ArrayList mainList,
                           @LayoutRes int progressBarLayout) {

        this.adapter = adapter;
        this.viewHolderClass = viewHolderClass;
        this.mainList = mainList;

        this.progressBarLayout = progressBarLayout;

        setRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        /*if (position >= images.size())
            return VIEW_PROG;
        else
            return VIEW_ITEM;*/

        return mainList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            vh = adapter.onCreateViewHolder(parent, viewType);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    progressBarLayout, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (viewHolderClass.isInstance(holder)) {


            //holder.mImageView.setImageDrawable(mBitmaps.get(position));
            //VectorDrawable loading = (VectorDrawable) mContext.getResources().getDrawable(R.drawable.ic_loading);

            adapter.onBindViewHolder(holder, position);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        if (mainList.get(mainList.size()-1) == null) {
            mainList.remove(mainList.size() - 1);
            notifyItemRemoved(mainList.size());
        }
        loading = false;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        recyclerView.setAdapter(this);
        /*LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);*/

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            /*if (!loading
                                    && totalItemCount <= (lastVisibleItem + LoadMoreAdapter.this.visibleThreshold)) {*/
                            // boolean b = (lastVisibleItem % (singleFetchItemCount-1)) == 0;
                            boolean b = lastVisibleItem == totalItemCount-1;
                            Log.i(TAG, "lastVisibleItem = " + lastVisibleItem + ", b = " + b);
                            if (!loading && b) {
                                // End has been reached
                                // Do something

                                mainList.add(null);
                                notifyItemInserted(mainList.size()-1);
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            // v.getLayoutParams().width = MATCH_PARENT;
            progressBar = v.findViewById(R.id.progress_bar);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
