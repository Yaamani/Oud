package com.example.oud.user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.OudApi;
import com.huxq17.download.Pump;
import com.huxq17.download.config.DownloadConfig;
import com.huxq17.download.core.DownloadListener;
import com.huxq17.download.core.DownloadRequest;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class TrackListRecyclerViewAdapter extends RecyclerView.Adapter<TrackListRecyclerViewAdapter.TrackItemViewHolder> {

    private static final String TAG = TrackListRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;

    //private ArrayList<Track> mTracks = new ArrayList<>();
    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<String> mTrackImages = new ArrayList<>();
    private ArrayList<String> mTrackNames = new ArrayList<>();
    private ArrayList<Boolean> mLikedTracks = new ArrayList<>();
    private ArrayList<Boolean> mDownloaded = new ArrayList<>();


    private OnTrackClickListener mTrackClickListener;
    private OnTrackClickListener mAvailableOfflineClickListener;
    private OnTrackClickListener mHeartClickListener;

    private String baseUrl;
    // private OudApi oudApi;

    //private ArrayList<View> reorderingSeparators;

    public TrackListRecyclerViewAdapter(Context mContext,
                                        String baseUrl,
                                        OnTrackClickListener mTrackClickListener,
                                        OnTrackClickListener mAvailableOfflineClickListener,
                                        OnTrackClickListener mHeartClickListener) {
        this.mContext = mContext;
        // this.ids = ids;
        this.mTrackClickListener = mTrackClickListener;
        // this.mTrackImages = mTrackImages;
        // this.mTrackNames = mTrackNames;

        //reorderingSeparators = new ArrayList<>();
        // this.mLikedTracks = mLikedTracks;
        this.mAvailableOfflineClickListener = mAvailableOfflineClickListener;
        this.mHeartClickListener = mHeartClickListener;

        this.baseUrl = baseUrl;
        // oudApi = OudUtils.instantiateRetrofitOudApi(baseUrl);
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
        String url = mTrackImages.get(position);
        String fullUrl = OudUtils.convertImageToFullUrl(url);
        Glide.with(mContext)
                .load(fullUrl)
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

        if (mDownloaded.get(position))
            holder.mAvailableOffline.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));

        //holder.mHeart.setOnClickListener(mHeartClickListeners.get(position));

    }

    @Override
    public int getItemCount() {
        return mIds.size();
        // return mTracks.size();
    }

    /*public void hideAllReorderingSeparators() {
        Log.i(TAG, "hideAllReorderingSeparators: " + reorderingSeparators.size());
        for (View separator : reorderingSeparators) {
            separator.setVisibility(View.INVISIBLE);
        }
    }*/

    public ArrayList<String> getmIds() {
        return mIds;
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

    public OnTrackClickListener getTrackClickListener() {
        return mTrackClickListener;
    }

    public OnTrackClickListener getAvailableOfflineClickListener() {
        return mAvailableOfflineClickListener;
    }

    public OnTrackClickListener getHeartClickListener() {
        return mHeartClickListener;
    }

    public void addTrack(String id,
                         String image,
                         String name,
                         Boolean isLiked) {
        addTrack(mIds.size(),
                id,
                image,
                name,
                isLiked);
    }

    public void addTrack(int position,
                         String trackId,
                         String trackImage,
                         String trackName,
                         boolean isLiked) {
        //mTrackClickListener.add(position, trackClickListener);
        mIds.add(trackId);
        mTrackImages.add(position, trackImage);
        mTrackNames.add(position, trackName);
        mLikedTracks.add(position, isLiked);
        mDownloaded.add(OudUtils.isDownloaded(trackId));

        //mHeartClickListener.add(position, heartClickListener);
    }

    public void removeTrack(int position) {
        //mTrackClickListener.remove(position);
        mIds.remove(position);
        mTrackImages.remove(position);
        mTrackNames.remove(position);
        mLikedTracks.remove(position);
        mDownloaded.remove(position);
        //mHeartClickListener.remove(position);
    }

    public void swapTracks(int i, int j) {
        //Collections.swap(mTrackClickListener, i, j);
        Collections.swap(mIds, i, j);
        Collections.swap(mTrackImages, i, j);
        Collections.swap(mTrackNames, i, j);
        Collections.swap(mLikedTracks, i, j);
        Collections.swap(mDownloaded, i, j);
        //Collections.swap(mHeartClickListener, i, j);
    }

    public class TrackItemViewHolder extends RecyclerView.ViewHolder {

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
            mAvailableOffline.setOnClickListener(v -> {
                this.availableOfflineClickListener.onTrackClickListener(getAdapterPosition(), v);
                String id = mIds.get(getAdapterPosition());
                String userId = OudUtils.getUserId(mContext);
                String filePath = userId + '/' + id;
                File file = new File(mContext.getExternalCacheDir().getAbsolutePath(), filePath);
                Pump.newRequest(baseUrl + "tracks/" + id + "/download", file.getAbsolutePath())
                        .setId(filePath)
                        .listener(new DownloadListener() {
                            @Override
                            public void onSuccess() {
                                super.onSuccess();
                                Log.d(TAG, "onSuccess: ");
                            }

                            @Override
                            public void onFailed() {
                                super.onFailed();
                                Log.d(TAG, "onFailed: " + getDownloadInfo().getStatus());
                            }

                            @Override
                            public void onProgress(int progress) {
                                super.onProgress(progress);
                                Log.d(TAG, "onProgress: " + progress);
                            }
                        })
                        .submit();
            });
            mHeart.setOnClickListener(v -> this.heartClickListener.onTrackClickListener(getAdapterPosition(), v));
        }
    }

    public interface OnTrackClickListener {
        void onTrackClickListener(int position, View view);
    }
}
