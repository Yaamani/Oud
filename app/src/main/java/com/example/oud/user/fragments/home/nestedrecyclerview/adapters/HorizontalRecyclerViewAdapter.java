package com.example.oud.user.fragments.home.nestedrecyclerview.adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.oud.OudUtils;
import com.example.oud.R;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.InnerItemViewHolder> {

    private static final String TAG = HorizontalRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;

    @LayoutRes
    private int itemLayout;

    private ArrayList<View.OnClickListener> clickListeners;

    private ArrayList<String> images;
    private ArrayList<Boolean> circularImages;
    private ArrayList<String> titles;
    private ArrayList<String> subtitles;

    private ArrayList<HashMap<String, Object>> relatedInfo;


    //private boolean circularImages;

    public HorizontalRecyclerViewAdapter(Context mContext,
                                         int itemLayout,
                                         ArrayList<View.OnClickListener> clickListeners,
                                         ArrayList<String> images,
                                         ArrayList<Boolean> circularImages,
                                         ArrayList<String> titles,
                                         ArrayList<String> subtitles,
                                         ArrayList<HashMap<String, Object>> relatedInfo) {
        this.mContext = mContext;
        this.itemLayout = itemLayout;

        this.clickListeners = clickListeners;
        this.circularImages = circularImages;
        this.images = images;
        this.titles = titles;
        this.subtitles = subtitles;

        this.relatedInfo = relatedInfo;
    }

    @NonNull
    @Override
    public InnerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new InnerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerItemViewHolder holder, int position) {

        //holder.mImageView.setImageDrawable(mBitmaps.get(position));
        //VectorDrawable loading = (VectorDrawable) mContext.getResources().getDrawable(R.drawable.ic_loading);

        holder.mLayout.setOnClickListener(clickListeners.get(position));

        //if (!mImages.get(position).equals(""))
        String iconTagPrefix = mContext.getResources().getString(R.string.tag_home_inner_item_image);
        holder.mImage.setTag(iconTagPrefix + position);
        String fullUrl = OudUtils.convertImageToFullUrl(images.get(position));
        Log.d(TAG, "onBindViewHolder: " + fullUrl);
        if (!circularImages.get(position))
            //OudUtils.glideBuilder(mContext, fullUrl)
            OudUtils.glideBuilder(mContext, fullUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_oud_loading)
                    //.error(R.drawable.ic_warning)
                    .into(holder.mImage);
        else
            OudUtils.glideBuilder(mContext, fullUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_oud_loading_circular)
                    //.error(R.drawable.ic_warning)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.mImage);

        String titleTagPrefix = mContext.getResources().getString(R.string.tag_home_inner_item_title);
        holder.mTitle.setTag(titleTagPrefix + position);
        holder.mTitle.setText(titles.get(position));
        if (circularImages.get(position)) {
            holder.mTitle.setGravity(Gravity.CENTER);
        }

        String subtitleTagPrefix = mContext.getResources().getString(R.string.tag_home_inner_item_subtitle);
        holder.mSubTitle.setTag(subtitleTagPrefix + position);
        holder.mSubTitle.setText(subtitles.get(position));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull InnerItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        /*if (holder.mSubTitle.getText().equals("")) {
            holder.mImage.getLayoutParams().width = 0;
            holder.mImage.getLayoutParams().height = 0;
        }*/
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class InnerItemViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mLayout;

        private ImageView mImage;
        private TextView mTitle;
        private TextView mSubTitle;

        public InnerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.relative_layout_item_inner);

            mImage = itemView.findViewById(R.id.image_item_inner);
            mTitle = itemView.findViewById(R.id.txt_item_inner_title);
            mSubTitle = itemView.findViewById(R.id.txt_item_inner_sub_title);
        }

        public RelativeLayout getmLayout() {
            return mLayout;
        }

        public ImageView getmImage() {
            return mImage;
        }

        public TextView getmTitle() {
            return mTitle;
        }

        public TextView getmSubTitle() {
            return mSubTitle;
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
}
