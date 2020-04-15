package com.example.oud.user;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.oud.R;
import com.example.oud.user.fragments.home.nestedrecyclerview.adapters.HorizontalRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadMoreAdapter extends RecyclerView.Adapter {

    private static final String TAG = LoadMoreAdapter.class.getSimpleName();

    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;


    private RecyclerView recyclerView;


    private int singleFetchItemCount;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    private Context mContext;

    private ArrayList<View.OnClickListener> clickListeners;

    private ArrayList<String> images;
    private ArrayList<Boolean> circularImages;
    private ArrayList<String> titles;
    private ArrayList<String> subtitles;

    private ArrayList<HashMap<String, Object>> relatedInfo;


    public LoadMoreAdapter(RecyclerView recyclerView,
                           int singleFetchItemCount,
                           Context mContext,
                           ArrayList<View.OnClickListener> clickListeners,
                           ArrayList<String> images,
                           ArrayList<Boolean> circularImages,
                           ArrayList<String> titles,
                           ArrayList<String> subtitles,
                           ArrayList<HashMap<String, Object>> relatedInfo) {



        this.singleFetchItemCount = singleFetchItemCount;

        this.mContext = mContext;

        this.clickListeners = clickListeners;
        this.images = images;
        this.circularImages = circularImages;
        this.titles = titles;
        this.subtitles = subtitles;

        this.relatedInfo = relatedInfo;

        setRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        /*if (position >= images.size())
            return VIEW_PROG;
        else
            return VIEW_ITEM;*/

        return images.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_inner, parent, false);

            vh = new HorizontalRecyclerViewAdapter.InnerItemViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HorizontalRecyclerViewAdapter.InnerItemViewHolder) {


            //holder.mImageView.setImageDrawable(mBitmaps.get(position));
            //VectorDrawable loading = (VectorDrawable) mContext.getResources().getDrawable(R.drawable.ic_loading);

            HorizontalRecyclerViewAdapter.InnerItemViewHolder h = (HorizontalRecyclerViewAdapter.InnerItemViewHolder) holder;

            h.getmLayout().setOnClickListener(clickListeners.get(position));

            //if (!mImages.get(position).equals(""))
            String iconTagPrefix = mContext.getResources().getString(R.string.tag_home_inner_item_image);
            h.getmImage().setTag(iconTagPrefix + position);
            if (!circularImages.get(position))
                Glide.with(mContext)
                        .load(images.get(position))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.ic_oud_loading)
                        //.error(R.drawable.ic_warning)
                        .into(h.getmImage());
            else
                Glide.with(mContext)
                        .load(images.get(position))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.ic_oud_loading_circular)
                        //.error(R.drawable.ic_warning)
                        .apply(RequestOptions.circleCropTransform())
                        .into(h.getmImage());

            String titleTagPrefix = mContext.getResources().getString(R.string.tag_home_inner_item_title);
            h.getmTitle().setTag(titleTagPrefix + position);
            h.getmTitle().setText(titles.get(position));
            if (circularImages.get(position)) {
                h.getmTitle().setGravity(Gravity.CENTER);
            }

            String subtitleTagPrefix = mContext.getResources().getString(R.string.tag_home_inner_item_subtitle);
            h.getmSubTitle().setTag(subtitleTagPrefix + position);
            h.getmSubTitle().setText(subtitles.get(position));

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        if (images.get(images.size()-1) == null) {
            images.remove(images.size() - 1);
            notifyItemRemoved(images.size());
        }
        loading = false;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        recyclerView.setAdapter(this);

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
                                images.add(null);
                                notifyItemInserted(images.size()-1);
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    public ArrayList<View.OnClickListener> getClickListeners() {
        return clickListeners;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<Boolean> getCircularImages() {
        return circularImages;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public ArrayList<String> getSubtitles() {
        return subtitles;
    }

    public ArrayList<HashMap<String, Object>> getRelatedInfo() {
        return relatedInfo;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar1);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
