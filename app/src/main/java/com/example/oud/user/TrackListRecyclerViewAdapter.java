package com.example.oud.user;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.user.fragments.premium.database.DownloadedTrack;
import com.example.oud.user.fragments.premium.database.DownloadedTracksDatabase;
import com.huxq17.download.Pump;
import com.huxq17.download.core.DownloadInfo;
import com.huxq17.download.core.DownloadListener;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class TrackListRecyclerViewAdapter extends RecyclerView.Adapter<TrackListRecyclerViewAdapter.TrackItemViewHolder> {

    private static final String TAG = TrackListRecyclerViewAdapter.class.getSimpleName();

    private Context mContext;

    //private ArrayList<Track> mTracks = new ArrayList<>();
    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<String> mTrackImages = new ArrayList<>();
    private ArrayList<String> mTrackNames = new ArrayList<>();
    private ArrayList<Boolean> mLikedTracks = new ArrayList<>();
    private ArrayList<DownloadInfo> mDownloadInfos = new ArrayList<>();


    private OnTrackClickListener mTrackClickListener;
    private OnTrackClickListener mAvailableOfflineClickListener;
    private OnTrackClickListener mHeartClickListener;

    private String baseUrl;
    private String loggedInUserId;
    //private String userType;
    // private OudApi oudApi;

    private Toast mToastDownloadFailed;

    private RecyclerView recyclerView;

    private boolean hideAllExceptDownloadButton;
    //private ArrayList<View> reorderingSeparators;

    public TrackListRecyclerViewAdapter(Context mContext,
                                        RecyclerView recyclerView, String oudBaseUrl,
                                        String loggedInUserId,
                                        OnTrackClickListener mTrackClickListener,
                                        OnTrackClickListener mAvailableOfflineClickListener,
                                        OnTrackClickListener mHeartClickListener,
                                        boolean hideAllExceptDownloadButton) {
        this.mContext = mContext;
        // this.ids = ids;
        this.mTrackClickListener = mTrackClickListener;
        // this.mTrackImages = mTrackImages;
        // this.mTrackNames = mTrackNames;

        //reorderingSeparators = new ArrayList<>();
        // this.mLikedTracks = mLikedTracks;
        this.mAvailableOfflineClickListener = mAvailableOfflineClickListener;
        this.mHeartClickListener = mHeartClickListener;

        this.recyclerView = recyclerView;

        this.baseUrl = oudBaseUrl;
        this.loggedInUserId = loggedInUserId;
        // oudApi = OudUtils.instantiateRetrofitOudApi(oudBaseUrl);
        this.hideAllExceptDownloadButton = hideAllExceptDownloadButton;

        downloadListener.enable();
    }

    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            super.onProgress(progress);
            DownloadInfo downloadInfo = getDownloadInfo();
            //TrackItemViewHolder holder = (TrackItemViewHolder) downloadInfo.getExtraData();

            int pos = mIds.indexOf(downloadInfo.getId());

            if (pos >= 0) {
                View view = recyclerView.getChildAt(pos);
                if (view != null) {
                    TrackItemViewHolder holder = (TrackItemViewHolder) recyclerView.getChildViewHolder(view);

                    if (holder != null) {
                        ProgressBar progressBar = holder.mProgressBarDownload;
                        progressBar.setProgress(progress);
                    }
                }
            }
        }

        @Override
        public void onSuccess() {
            super.onSuccess();
            DownloadInfo downloadInfo = getDownloadInfo();

            int pos = mIds.indexOf(downloadInfo.getId());

            if (pos >= 0) {
                View view = recyclerView.getChildAt(pos);
                if (view != null) {
                    TrackItemViewHolder holder = (TrackItemViewHolder) recyclerView.getChildViewHolder(view);

                    if (holder != null) {
                        holder.mAvailableOffline.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));
                        holder.mProgressBarDownload.setVisibility(View.GONE);
                        holder.mAvailableOffline.setAlpha(1.0f);
                    }
                }
            }
        }

        @Override
        public void onFailed() {
            super.onFailed();
            DownloadInfo downloadInfo = getDownloadInfo();


            if (mToastDownloadFailed == null) {
                mToastDownloadFailed = Toast.makeText(mContext, R.string.download_failed, Toast.LENGTH_SHORT);
            }
            mToastDownloadFailed.show();

            int pos = mIds.indexOf(downloadInfo.getId());
            View view = recyclerView.getChildAt(pos);
            if (view != null) {
                if (pos >= 0) {
                    TrackItemViewHolder holder = (TrackItemViewHolder) recyclerView.getChildViewHolder(view);

                    if (holder != null) {
                        holder.mProgressBarDownload.setVisibility(View.GONE);
                        holder.mAvailableOffline.setAlpha(1.0f);
                    }
                }
            }



        }
    };

    public void disableDownloadListener() {
        downloadListener.disable();
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
        OudUtils.glideBuilder(mContext, fullUrl)
                .placeholder(R.drawable.ic_oud_loading)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(holder.mTrackImage);


        String titleTagPrefix = mContext.getResources().getString(R.string.tag_track_list_adapter_title);
        holder.mTrackName.setTag(titleTagPrefix + position);
        holder.mTrackName.setText(mTrackNames.get(position));

        if (hideAllExceptDownloadButton)
            holder.mHeart.setVisibility(View.GONE);
        else {

            if (mLikedTracks.get(position))
                //holder.mHeart.setImageResource(R.drawable.ic_heart_selected);
                holder.mHeart.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));

            String heartTagPrefix = mContext.getResources().getString(R.string.tag_track_list_adapter_heart);
            holder.mHeart.setTag(heartTagPrefix + position);
        }


        DownloadInfo currentDownloadInfo = mDownloadInfos.get(position);
        if (currentDownloadInfo != null) {
            //currentDownloadInfo.setExtraData(holder);
            if (currentDownloadInfo.getStatus() == DownloadInfo.Status.FINISHED)
                holder.mAvailableOffline.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));
            else if (currentDownloadInfo.getStatus() == DownloadInfo.Status.RUNNING) {
                holder.mAvailableOffline.setAlpha(0.5f);
                holder.mProgressBarDownload.setVisibility(View.VISIBLE);
                holder.mProgressBarDownload.setProgress(currentDownloadInfo.getProgress());
            }
        }




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

    /*public ArrayList<String> getTrackImages() {
        return mTrackImages;
    }

    public ArrayList<String> getTrackNames() {
        return mTrackNames;
    }

    public ArrayList<Boolean> getLikedTracks() {
        return mLikedTracks;
    }*/

    public String getId(int position) {
        return mIds.get(position);
    }

    public String getImage(int position) {
        return mTrackImages.get(position);
    }

    public String getName(int position) {
        return mTrackNames.get(position);
    }

    public boolean isLiked(int position) {
        return mLikedTracks.get(position);
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
        mIds.add(position, trackId);
        mTrackImages.add(position, trackImage);
        mTrackNames.add(position, trackName);
        mLikedTracks.add(position, isLiked);
        mDownloadInfos.add(position, OudUtils.getTrackDownloadInfo(loggedInUserId, trackId));

        //mHeartClickListener.add(position, heartClickListener);
    }

    public void setTrack(int position,
                         String trackId,
                         String trackImage,
                         String trackName,
                         boolean isLiked) {
        //mTrackClickListener.add(position, trackClickListener);
        mIds.set(position, trackId);
        mTrackImages.set(position, trackImage);
        mTrackNames.set(position, trackName);
        mLikedTracks.set(position, isLiked);
        mDownloadInfos.set(position, OudUtils.getTrackDownloadInfo(loggedInUserId, trackId));


        //mHeartClickListener.add(position, heartClickListener);
    }

    public void removeTrack(int position) {
        //mTrackClickListener.remove(position);
        mIds.remove(position);
        mTrackImages.remove(position);
        mTrackNames.remove(position);
        mLikedTracks.remove(position);
        mDownloadInfos.remove(position);
        //mHeartClickListener.remove(position);
    }

    public void swapTracks(int i, int j) {
        //Collections.swap(mTrackClickListener, i, j);
        Collections.swap(mIds, i, j);
        Collections.swap(mTrackImages, i, j);
        Collections.swap(mTrackNames, i, j);
        Collections.swap(mLikedTracks, i, j);
        Collections.swap(mDownloadInfos, i, j);
        //Collections.swap(mHeartClickListener, i, j);
    }

    public class TrackItemViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout mLayout;
        private ImageView mTrackImage;
        private TextView mTrackName;
        private ImageButton mHeart;
        private ImageButton mAvailableOffline;
        private ProgressBar mProgressBarDownload;

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
            mProgressBarDownload = itemView.findViewById(R.id.progress_bar_track_download);

            mLayout.setOnClickListener(v -> {
                if (this.trackClickListener != null)
                    this.trackClickListener.onTrackClickListener(getAdapterPosition(), v);
            });
            mAvailableOffline.setOnClickListener(v -> {
                if (this.availableOfflineClickListener != null)
                    this.availableOfflineClickListener.onTrackClickListener(getAdapterPosition(), v);

                //DownloadInfo downloadInfo = mDownloadInfos.get(getAdapterPosition());
                if (mAvailableOffline.getAlpha() <= 0.5f)
                    return;

                mAvailableOffline.setAlpha(0.5f);

                if (/*downloadInfo != null*/Pump.getFileIfSucceed(mIds.get(getAdapterPosition())) != null)
                    deleteTrack();
                else {
                    downloadTrack();
                }

            });
            mHeart.setOnClickListener(v -> {
                if (this.heartClickListener != null)
                    this.heartClickListener.onTrackClickListener(getAdapterPosition(), v);
            });
        }

        private void downloadTrack() {

            int pos = getAdapterPosition();
            String id = mIds.get(pos);
            String name = mTrackNames.get(pos);
            String image = mTrackImages.get(pos);
            //String filePath = userId + '/' + id;
            File file = new File(mContext.getExternalCacheDir().getAbsolutePath());

            mProgressBarDownload.setVisibility(View.VISIBLE);

            Pump.newRequest(baseUrl + "tracks/" + id + "/download"/*, file.getAbsolutePath()*/)
                    //Pump.newRequest("https://server10.mp3quran.net/ajm/128/001.mp3")
                    .setId(id)
                    //.forceReDownload(true)
                    .tag(loggedInUserId)
                    .listener(new DownloadListener() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();
                            DownloadInfo downloadInfo = getDownloadInfo();
                            if (downloadInfo.getId().equals(id)) {
                                DownloadedTrack downloadedTrack = new DownloadedTrack();
                                downloadedTrack.id = id;
                                downloadedTrack.name = name;
                                downloadedTrack.image = image;
                                downloadedTrack.filePath = downloadInfo.getFilePath();

                                new InsertDownloadedTrackIntoRoomDataBase(mContext).execute(downloadedTrack);
                            }
                        }
                    })
                    .submit();

            List<DownloadInfo> downloadInfoList = Pump.getDownloadListByTag(loggedInUserId);
            for (DownloadInfo downloadInfo : downloadInfoList) {
                if (downloadInfo.getId().equals(id)) {
                    downloadInfo.setExtraData(TrackItemViewHolder.this);
                    mDownloadInfos.set(getAdapterPosition(), downloadInfo);
                }
            }


        }

        private void deleteTrack() {
            String id = mIds.get(getAdapterPosition());
            new DeleteTrackAsyncTask().execute(id, mAvailableOffline);
        }


    }

    private static class DeleteTrackAsyncTask extends AsyncTask<Object, Void, ImageButton> {

        @Override
        protected ImageButton doInBackground(Object... params) {
            String id = (String) params[0];
            Pump.deleteById(id);
            return (ImageButton) params[1];
        }

        @Override
        protected void onPostExecute(ImageButton mAvailableOffline) {
            super.onPostExecute(mAvailableOffline);

            mAvailableOffline.setAlpha(1.0f);
            mAvailableOffline.setColorFilter(Color.WHITE);
        }
    }

    private static class InsertDownloadedTrackIntoRoomDataBase extends AsyncTask<DownloadedTrack, Void, Void> {

        private WeakReference<Context> mContext;

        public InsertDownloadedTrackIntoRoomDataBase(Context context) {
            this.mContext = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(DownloadedTrack... downloadedTracks) {
            DownloadedTracksDatabase downloadedTracksDatabase =
                    Room.databaseBuilder(mContext.get(), DownloadedTracksDatabase.class, Constants.DOWNLOADED_TRACKS_DATABASE_NAME).build();

            try {
                downloadedTracksDatabase.downloadedTrackDao().insert(downloadedTracks[0]);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public interface OnTrackClickListener {
        void onTrackClickListener(int position, View view);
    }
}
