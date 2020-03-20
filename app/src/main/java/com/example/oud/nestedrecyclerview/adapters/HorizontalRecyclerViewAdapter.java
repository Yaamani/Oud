package com.example.oud.nestedrecyclerview.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.oud.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.PlayerViewHolder> {

    private static final String TAG = HorizontalRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;

    private ArrayList<String> mImages;
    private ArrayList<String> mTitles;
    private ArrayList<String> mSubTitles;

    public HorizontalRecyclerViewAdapter(Context mContext, ArrayList<String> mImages, ArrayList<String> mTitles, ArrayList<String> mSubTitles) {
        this.mContext = mContext;
        this.mImages = mImages;
        this.mTitles = mTitles;
        this.mSubTitles = mSubTitles;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inner, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        //holder.mImageView.setImageDrawable(mBitmaps.get(position));
        //VectorDrawable loading = (VectorDrawable) mContext.getResources().getDrawable(R.drawable.ic_loading);


        Glide.with(mContext)
                .load(mImages.get(position))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_loading)
                .into(holder.mImage);
        holder.mTitle.setText(mTitles.get(position));
        holder.mSubTitle.setText(mSubTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mTitle;
        private TextView mSubTitle;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image_item_inner);
            mTitle = itemView.findViewById(R.id.txt_item_inner_title);
            mSubTitle = itemView.findViewById(R.id.txt_item_inner_sub_title);
        }
    }
}
