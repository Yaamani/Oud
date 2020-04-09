package com.example.oud.user;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.oud.R;
import com.example.oud.user.fragments.home.nestedrecyclerview.adapters.HorizontalRecyclerViewAdapter;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LoadMoreAdapter extends RecyclerView.Adapter {
    private static final int VIEW_ITEM = 1;
    private static final int VIEW_PROG = 0;

    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = /*Constants.ARTIST_ALBUMS_VISIBLE_THRESHOLD*/5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    private Context mContext;

    private ArrayList<View.OnClickListener> clickListeners;

    private ArrayList<String> images;
    private ArrayList<String> titles;
    private ArrayList<String> subtitles;

    private boolean circularImages;



    public LoadMoreAdapter(RecyclerView recyclerView,
                           Context mContext,
                           ArrayList<View.OnClickListener> clickListeners,
                           ArrayList<String> images,
                           ArrayList<String> titles,
                           ArrayList<String> subtitles,
                           boolean circularImages) {

        this.mContext = mContext;

        this.clickListeners = clickListeners;
        this.images = images;
        this.titles = titles;
        this.subtitles = subtitles;

        this.circularImages = circularImages;


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
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
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
    public int getItemViewType(int position) {
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
            if (!circularImages)
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
            if (circularImages) {
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
        loading = false;
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
