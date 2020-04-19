package com.example.oud.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.R;


import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class TrackListRecyclerViewAdapter extends RecyclerView.Adapter<TrackListRecyclerViewAdapter.TrackItemViewHolder> {

    private static final String TAG = TrackListRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;

    private ArrayList<String> ids;

    private OnTrackClickListener mTrackClickListener;
    private ArrayList<String> mTrackImages;
    private ArrayList<String> mTrackNames;
    private ArrayList<Boolean> mLikedTracks;
    private OnTrackClickListener mAvailableOfflineClickListener;
    private OnTrackClickListener mHeartClickListener;

    //private ArrayList<View> reorderingSeparators;

    public TrackListRecyclerViewAdapter(Context mContext,
                                        ArrayList<String> ids,
                                        OnTrackClickListener mTrackClickListener,
                                        ArrayList<String> mTrackImages,
                                        ArrayList<String> mTrackNames,
                                        ArrayList<Boolean> mLikedTracks,
                                        OnTrackClickListener mAvailableOfflineClickListener,
                                        OnTrackClickListener mHeartClickListener) {
        this.mContext = mContext;
        this.ids = ids;
        this.mTrackClickListener = mTrackClickListener;
        this.mTrackImages = mTrackImages;
        this.mTrackNames = mTrackNames;

        //reorderingSeparators = new ArrayList<>();
        this.mLikedTracks = mLikedTracks;
        this.mAvailableOfflineClickListener = mAvailableOfflineClickListener;
        this.mHeartClickListener = mHeartClickListener;
    }

    @NonNull
    @Override
    public TrackItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_playlist_track, parent, false);

        /*reorderingSeparators.add(view.findViewById(R.id.track_reorder_separator_above));
        reorderingSeparators.add(view.findViewById(R.id.track_reorder_separator_below));*/

        return new TrackItemViewHolder(view, mTrackClickListener, mHeartClickListener, mAvailableOfflineClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackItemViewHolder holder, int position) {

        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        //holder.mLayout.setOnClickListener(mTrackClickListeners.get(position));

        Glide.with(mContext)
                .load(mTrackImages.get(position))
                .placeholder(R.drawable.ic_oud_loading)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(holder.mTrackImage);


        String titleTagPrefix = mContext.getResources().getString(R.string.tag_track_list_adapter_title);
        holder.mTrackName.setTag(titleTagPrefix + position);
        holder.mTrackName.setText(mTrackNames.get(position));

        if (mLikedTracks.get(position))
            //holder.mHeart.setImageResource(R.drawable.ic_heart_selected);
            holder.mHeart.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));

        String heartTagPrefix = mContext.getResources().getString(R.string.tag_track_list_adapter_heart);
        holder.mHeart.setTag(heartTagPrefix + position);

        //holder.mHeart.setOnClickListener(mHeartClickListeners.get(position));

    }

    @Override
    public int getItemCount() {
        return mTrackNames.size();
    }



    /*public void hideAllReorderingSeparators() {
        Log.i(TAG, "hideAllReorderingSeparators: " + reorderingSeparators.size());
        for (View separator : reorderingSeparators) {
            separator.setVisibility(View.INVISIBLE);
        }
    }*/

    public ArrayList<String> getIds() {
        return ids;
    }

    public OnTrackClickListener getTrackClickListener() {
        return mTrackClickListener;
    }

    public ArrayList<String> getTrackImages() {
        return mTrackImages;
    }

    public ArrayList<String> getTrackNames() {
        return mTrackNames;
    }

    public ArrayList<Boolean> getLikedTracks() {
        return mLikedTracks;
    }

    public OnTrackClickListener getAvailableOfflineClickListener() {
        return mAvailableOfflineClickListener;
    }

    public OnTrackClickListener getHeartClickListener() {
        return mHeartClickListener;
    }

    public void addItem(int position,
                        String trackImage,
                        String trackName,
                        Boolean isLiked) {
        //mTrackClickListener.add(position, trackClickListener);
        mTrackImages.add(position, trackImage);
        mTrackNames.add(position, trackName);
        mLikedTracks.add(position, isLiked);
        //mHeartClickListener.add(position, heartClickListener);
    }

    public void removeItem(int position) {
        //mTrackClickListener.remove(position);
        mTrackImages.remove(position);
        mTrackNames.remove(position);
        mLikedTracks.remove(position);
        //mHeartClickListener.remove(position);
    }

    public void swapItems(int i, int j) {
        //Collections.swap(mTrackClickListener, i, j);
        Collections.swap(mTrackImages, i, j);
        Collections.swap(mTrackNames, i, j);
        Collections.swap(mLikedTracks, i, j);
        //Collections.swap(mHeartClickListener, i, j);
    }

    public static class TrackItemViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayout;
        private ImageView mTrackImage;
        private TextView mTrackName;
        private ImageButton mHeart;
        private ImageButton mAvailableOffline;


        private OnTrackClickListener trackClickListener;
        private OnTrackClickListener heartClickListener;
        private OnTrackClickListener availableOfflineClickListener;

        TrackItemViewHolder(@NonNull View itemView,
                            OnTrackClickListener trackClickListener,
                            OnTrackClickListener heartClickListener,
                            OnTrackClickListener availableOfflineClickListener) {
            super(itemView);

            this.trackClickListener = trackClickListener;
            this.heartClickListener = heartClickListener;
            this.availableOfflineClickListener = availableOfflineClickListener;


            mLayout = itemView.findViewById(R.id.layout_playlist_track);
            mTrackImage = itemView.findViewById(R.id.img_track_playlist);
            mTrackName = itemView.findViewById(R.id.txt_track_playlist);
            mTrackName.setSelected(true);
            mAvailableOffline = itemView.findViewById(R.id.btn_track_available_offline);
            mHeart = itemView.findViewById(R.id.btn_track_like);

            mLayout.setOnClickListener(v -> this.trackClickListener.onTrackClickListener(getAdapterPosition(), v));
            mAvailableOffline.setOnClickListener(v -> this.availableOfflineClickListener.onTrackClickListener(getAdapterPosition(), v));
            mHeart.setOnClickListener(v -> this.heartClickListener.onTrackClickListener(getAdapterPosition(), v));
        }
    }

    public interface OnTrackClickListener {
        void onTrackClickListener(int position, View view);
    }
}
