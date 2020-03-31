package com.example.oud.user.fragments.home.nestedrecyclerview.adapters;

import android.content.Context;
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

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter<HorizontalRecyclerViewAdapter.InnerItemViewHolder> {

    private static final String TAG = HorizontalRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;

    private ArrayList<String> images;
    private ArrayList<String> titles;
    private ArrayList<String> subtitles;

    public HorizontalRecyclerViewAdapter(Context mContext, ArrayList<String> images, ArrayList<String> titles, ArrayList<String> subtitles) {
        this.mContext = mContext;
        this.images = images;
        this.titles = titles;
        this.subtitles = subtitles;
    }

    @NonNull
    @Override
    public InnerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inner, parent, false);
        return new InnerItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerItemViewHolder holder, int position) {

        //holder.mImageView.setImageDrawable(mBitmaps.get(position));
        //VectorDrawable loading = (VectorDrawable) mContext.getResources().getDrawable(R.drawable.ic_loading);

        //if (!mImages.get(position).equals(""))
        String iconTagPrefix = mContext.getResources().getString(R.string.tag_home_inner_item_image);
        holder.mImage.setTag(iconTagPrefix + position);
        Glide.with(mContext)
                .load(images.get(position))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_loading)
                //.error(R.drawable.ic_warning)
                .into(holder.mImage);

        String titleTagPrefix = mContext.getResources().getString(R.string.tag_home_inner_item_title);
        holder.mTitle.setTag(titleTagPrefix + position);
        holder.mTitle.setText(titles.get(position));

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

        private ImageView mImage;
        private TextView mTitle;
        private TextView mSubTitle;

        public InnerItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.image_item_inner);
            mTitle = itemView.findViewById(R.id.txt_item_inner_title);
            mSubTitle = itemView.findViewById(R.id.txt_item_inner_sub_title);
        }
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public ArrayList<String> getSubtitles() {
        return subtitles;
    }
}
