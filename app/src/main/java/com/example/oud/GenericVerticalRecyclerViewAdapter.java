package com.example.oud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

import java.util.ArrayList;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class GenericVerticalRecyclerViewAdapter extends RecyclerView.Adapter<GenericVerticalRecyclerViewAdapter.GenericVerticalAdapterViewHolder> {

    private Context mContext;

    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<Boolean> mCircularImages = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<Boolean> mImageButtonSelected = new ArrayList<>();

    @DrawableRes
    private int mImageButtonDrawableId;

    private OnItemClickListener mItemClickListener;
    private OnItemClickListener mImageButtonClickListener;

    public GenericVerticalRecyclerViewAdapter(Context mContext,
                                              int mImageButtonDrawableId,
                                              OnItemClickListener mItemClickListener,
                                              OnItemClickListener mImageButtonClickListener) {
        this.mContext = mContext;

        this.mImageButtonDrawableId = mImageButtonDrawableId;
        this.mItemClickListener = mItemClickListener;
        this.mImageButtonClickListener = mImageButtonClickListener;
    }

    @NonNull
    @Override
    public GenericVerticalAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_generic_vertical_adapter_item, parent, false);
        return new GenericVerticalAdapterViewHolder(view, mItemClickListener, mImageButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericVerticalAdapterViewHolder holder, int position) {
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        String fullUrl = OudUtils.convertImageToFullUrl(mImages.get(position));
        if (!mCircularImages.get(position))
            OudUtils.glideBuilder(mContext, fullUrl)
                    .placeholder(R.drawable.ic_oud_loading)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(holder.mImageView);
        else
            OudUtils.glideBuilder(mContext, fullUrl)
                    .placeholder(R.drawable.ic_oud_loading_circular)
                    .apply(RequestOptions.circleCropTransform())
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(holder.mImageView);

        String titleTagPrefix = mContext.getResources().getString(R.string.tag_generic_vertical_item_title);
        holder.mTextViewName.setTag(titleTagPrefix + position);
        holder.mTextViewName.setText(mTitles.get(position));

        String imageButtonTagPrefix = mContext.getResources().getString(R.string.tag_generic_vertical_item_btn);
        holder.mImageButton.setTag(imageButtonTagPrefix + position);
        holder.mImageButton.setImageResource(mImageButtonDrawableId);
        if (mImageButtonSelected.get(position))
            holder.mImageButton.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));

    }

    @Override
    public int getItemCount() {
        return mIds.size();
    }

    /**
     *
     * @param id
     * @param image
     * @param circularImage
     * @param title
     * @param imageButtonSelected
     */
    public void addItem(String id,
                        String image,
                        boolean circularImage,
                        String title,
                        boolean imageButtonSelected) {
        addItem(getItemCount(),
                id,
                image,
                circularImage,
                title,
                imageButtonSelected);
    }

    public void addItem(int position,
                        String id,
                        String image,
                        boolean circularImage,
                        String title,
                        boolean imageButtonSelected) {
        mIds.add(position, id);
        mImages.add(position, image);
        mCircularImages.add(position, circularImage);
        mTitles.add(position, title);
        mImageButtonSelected.add(position, imageButtonSelected);
    }

    public void setItem(int position,
                         String id,
                         String image,
                         boolean circularImage,
                         String title,
                         boolean imageButtonSelected) {

        mIds.set(position, id);
        mImages.set(position, image);
        mCircularImages.set(position, circularImage);
        mTitles.set(position, title);
        mImageButtonSelected.set(position, imageButtonSelected);
    }

    public void removeItem(int position) {
        mIds.remove(position);
        mImages.remove(position);
        mCircularImages.remove(position);
        mTitles.remove(position);
        mImageButtonSelected.remove(position);
    }

    public ArrayList<String> getIds() {
        return mIds;
    }

    public String getId(int position) {
        return mIds.get(position);
    }

    public String getImage(int position) {
        return mImages.get(position);
    }

    public boolean isCircularImage(int position) {
        return mCircularImages.get(position);
    }

    public String getTitle(int position) {
        return mTitles.get(position);
    }

    public boolean isImageButtonSelected(int position) {
        return mImageButtonSelected.get(position);
    }

    public int getImageButtonDrawableId() {
        return mImageButtonDrawableId;
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public OnItemClickListener getImageButtonClickListener() {
        return mImageButtonClickListener;
    }

    public static class GenericVerticalAdapterViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayout;
        private ImageView mImageView;
        private TextView mTextViewName;
        private ImageButton mImageButton;

        private OnItemClickListener mItemClickListener;
        private OnItemClickListener mImageButtonClickListener;

        public GenericVerticalAdapterViewHolder(@NonNull View itemView,
                                                OnItemClickListener itemClickListener,
                                                OnItemClickListener imageButtonClickListener) {
            super(itemView);

            this.mItemClickListener = itemClickListener;
            this.mImageButtonClickListener = imageButtonClickListener;

            mLayout = itemView.findViewById(R.id.layout_generic_vertical_adapter_item);
            mImageView = itemView.findViewById(R.id.img_generic_vertical_adapter_item);
            mTextViewName = itemView.findViewById(R.id.txt_generic_vertical_adapter_item);
            mImageButton = itemView.findViewById(R.id.btn_generic_vertical_adapter_item);

            mLayout.setOnClickListener(v -> GenericVerticalAdapterViewHolder.this.mItemClickListener.onItemClickListener(getAdapterPosition(), v));
            mImageButton.setOnClickListener(v -> GenericVerticalAdapterViewHolder.this.mImageButtonClickListener.onItemClickListener(getAdapterPosition(), v));
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, View view);
    }
}
