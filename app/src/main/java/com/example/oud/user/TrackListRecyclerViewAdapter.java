package com.example.oud.user;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
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
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.user.fragments.premium.DownloadService;
import com.example.oud.user.fragments.premium.database.DownloadedTrack;
import com.example.oud.user.fragments.premium.database.DownloadedTrackDaoHelper;
import com.example.oud.user.fragments.premium.database.DownloadedTracksDatabase;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class TrackListRecyclerViewAdapter extends RecyclerView.Adapter<TrackListRecyclerViewAdapter.TrackItemViewHolder> {

    private static final String TAG = TrackListRecyclerViewAdapter.class.getSimpleName();

    private static LinkedList<DownloadedTrack> currentlyBeingDownloadedTracks;

    private Context mContext;

    //private ArrayList<Track> mTracks = new ArrayList<>();
    private ArrayList<String> mIds = new ArrayList<>();
    private ArrayList<String> mTrackImages = new ArrayList<>();
    private ArrayList<String> mTrackNames = new ArrayList<>();
    private ArrayList<Boolean> mLikedTracks = new ArrayList<>();
    //private ArrayList<DownloadInfo> mDownloadInfos = new ArrayList<>();
    private ArrayList<Boolean> mDownloaded = new ArrayList<>();

    //private ArrayList<DownloadedTrack> mDownloadedTracks = new ArrayList<>();
    //private ArrayList<Boolean> mDownloadedTrackFetched = new ArrayList<>();


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

    private DownloadService.DownloadServiceBinder downloadServiceBinder;

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

        // downloadListener.enable();

        Intent downloadServiceBindIntent =  new Intent(mContext, DownloadService.class);
        mContext.bindService(downloadServiceBindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
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


        // if (mDownloadedTracks.get(position) == null)

        new AvailableOfflineImageButtonHandler(mContext, holder, mDownloaded, mIds.get(position), position).execute();

        /*DownloadInfo currentDownloadInfo = mDownloadInfos.get(position);
        if (currentDownloadInfo != null) {
            //currentDownloadInfo.setExtraData(holder);
            if (currentDownloadInfo.getStatus() == DownloadInfo.Status.FINISHED)
                holder.mAvailableOffline.setColorFilter(mContext.getResources().getColor(R.color.colorPrimary));
            else if (currentDownloadInfo.getStatus() == DownloadInfo.Status.RUNNING) {
                holder.mAvailableOffline.setAlpha(0.5f);
                holder.mProgressBarDownload.setVisibility(View.VISIBLE);
                holder.mProgressBarDownload.setProgress(currentDownloadInfo.getProgress());
            }
        }*/




        //holder.mHeart.setOnClickListener(mHeartClickListeners.get(position));

    }

    @Override
    public int getItemCount() {
        return mIds.size();
        // return mTracks.size();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder iBinder) {
            Log.d(TAG, "ServiceConnection: connected to service.");
            downloadServiceBinder = (DownloadService.DownloadServiceBinder) iBinder;

            downloadServiceBinder.getDownloadService().addDownloadListener(downloadListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            downloadServiceBinder = null;
        }
    };

    private DownloadService.DownloadListener downloadListener = new DownloadService.DownloadListener() {
        @Override
        public void onProgress(DownloadService.TrackDownloadInfo trackDownloadInfo, int progress) {
            // Log.d(TAG, "onProgress: " + progress);

            //TrackItemViewHolder holder = (TrackItemViewHolder) downloadInfo.getExtraData();

            int pos = mIds.indexOf(trackDownloadInfo.getTrackId());

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
        public void onSuccess(DownloadService.TrackDownloadInfo trackDownloadInfo) {

            Log.d(TAG, "DownloadListener: onSuccess: ");

            int pos = mIds.indexOf(trackDownloadInfo.getTrackId());

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
                mDownloaded.set(pos, true);
            }

            DownloadedTrack downloadedTrack = new DownloadedTrack();
            downloadedTrack.userId = trackDownloadInfo.getUserId();
            downloadedTrack.id = trackDownloadInfo.getTrackId();
            downloadedTrack.name = trackDownloadInfo.getTrackName();
            downloadedTrack.image = trackDownloadInfo.getTrackImage();
            downloadedTrack.fileName = trackDownloadInfo.getFileName();

            new DownloadedTrackDaoHelper.InsertDownloadedTrackIntoRoomDatabase(mContext, currentlyBeingDownloadedTracks).execute(downloadedTrack);
        }

        @Override
        public void onFailure(DownloadService.TrackDownloadInfo trackDownloadInfo, int reason) {

            Log.d(TAG, "DownloadListener: onFailure: ");

            if (reason == 403){
                Toast.makeText(mContext, "يا فقير", Toast.LENGTH_SHORT).show();
            } else {

                if (mToastDownloadFailed == null)
                    mToastDownloadFailed = Toast.makeText(mContext, R.string.download_failed, Toast.LENGTH_SHORT);
                mToastDownloadFailed.show();
            }

            int pos = mIds.indexOf(trackDownloadInfo.getTrackId());
            View view = recyclerView.getChildAt(pos);
            if (view != null) {
                if (pos >= 0) {
                    TrackItemViewHolder holder = (TrackItemViewHolder) recyclerView.getChildViewHolder(view);

                    if (holder != null) {
                        holder.mProgressBarDownload.setVisibility(View.GONE);
                        holder.mAvailableOffline.setAlpha(1.0f);
                    }
                    mDownloaded.set(pos, false);
                }
            }

            int i = 0;
            for (DownloadedTrack current : currentlyBeingDownloadedTracks) {
                if (current.id.equals(trackDownloadInfo.getTrackId())) {
                    currentlyBeingDownloadedTracks.remove(i);
                    break;
                }
                i++;
            }

        }
    };

    /*private DownloadListener downloadListener = new DownloadListener() {
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


            if (mToastDownloadFailed == null)
                mToastDownloadFailed = Toast.makeText(mContext, R.string.download_failed, Toast.LENGTH_SHORT);
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
    };*/

    public void preventLeaksBecauseOfDownloadService() {
        downloadServiceBinder.getDownloadService().removeDownloadListener(downloadListener);
        try {
            mContext.unbindService(serviceConnection);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }


    private static class AvailableOfflineImageButtonHandler extends AsyncTask<Void, Void, DownloadedTrack> {

        private WeakReference<Context> mContext;
        private WeakReference<TrackItemViewHolder> mHolder;
        private WeakReference<ArrayList<Boolean>> mDownloaded;
        private String trackId;
        private int mAdapterPosition;

        public AvailableOfflineImageButtonHandler(Context context, TrackItemViewHolder availableOffline, ArrayList<Boolean> downloaded, String trackId, int adapterPosition) {
            this.mContext = new WeakReference<>(context);
            this.mHolder = new WeakReference<>(availableOffline);
            this.mDownloaded = new WeakReference<>(downloaded);
            this.trackId = trackId;
            this.mAdapterPosition = adapterPosition;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mHolder.get().mAvailableOffline.setAlpha(0.5f);
        }

        @Override
        protected DownloadedTrack doInBackground(Void... params) {
            DownloadedTracksDatabase downloadedTracksDatabase = OudUtils.getDownloadedTracksDatabase(mContext.get());

            return downloadedTracksDatabase.downloadedTrackDao().getDownloadedTrack(trackId);
        }

        @Override
        protected void onPostExecute(DownloadedTrack downloadedTrack) {
            super.onPostExecute(downloadedTrack);

            mDownloaded.get().set(mAdapterPosition, false);

            if (downloadedTrack != null) {
                mHolder.get().mAvailableOffline.setColorFilter(mContext.get().getResources().getColor(R.color.colorPrimary));
                mHolder.get().mAvailableOffline.setAlpha(1.0f);

                mDownloaded.get().set(mAdapterPosition, true);
            } else {
                mHolder.get().mAvailableOffline.setAlpha(1.0f);
                /*DownloadInfo downloadInfo = Pump.getDownloadInfoById(trackId);
                if (downloadInfo != null)
                    if (downloadInfo.getStatus() == DownloadInfo.Status.RUNNING) {
                        mHolder.get().mAvailableOffline.setAlpha(0.5f);
                        mHolder.get().mProgressBarDownload.setVisibility(View.VISIBLE);
                        mHolder.get().mProgressBarDownload.setProgress(downloadInfo.getProgress());
                    }*/

                if (currentlyBeingDownloadedTracks != null)
                    for (DownloadedTrack current : currentlyBeingDownloadedTracks) {
                        if (trackId.equals(current.id)) {
                            mHolder.get().mAvailableOffline.setAlpha(0.5f);
                            mHolder.get().mProgressBarDownload.setVisibility(View.VISIBLE);
                            // mHolder.get().mProgressBarDownload.setProgress(downloadInfo.getProgress());
                        }
                    }
            }

        }
    }


    public static LinkedList<DownloadedTrack> getCurrentlyBeingDownloadedTracks() {
        /*LinkedList<DownloadedTrack> copy = new LinkedList<>();
        if (currentlyBeingDownloadedTracks != null)
            if (!currentlyBeingDownloadedTracks.isEmpty())
                Collections.copy(copy, currentlyBeingDownloadedTracks);*/
        if (currentlyBeingDownloadedTracks == null)
            currentlyBeingDownloadedTracks = new LinkedList<>();
        return /*copy*/currentlyBeingDownloadedTracks;
    }

    //private void handleAvailableOfflineButton(ImageButton button, )



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
        mDownloaded.add(position, null);
        // mDownloadInfos.add(position, OudUtils.getTrackDownloadInfo(loggedInUserId, trackId));
        /*new DownloadedTrackDaoHelper.GetDownloadedTrackFromRoomDatabase(mContext, mDownloadedTracks, position, Constants.ArrayOperation.ADD)
                .execute(trackId);*/

        //mDownloadedTracks.add(position, OudUtils.getDownloadedTrack(mContext, trackId));
        //mDownloadedTracks.add(position, null); // We'll fetch the database in onBindViewHolder()


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
        mDownloaded.set(position, null);

        // mDownloadInfos.set(position, OudUtils.getTrackDownloadInfo(loggedInUserId, trackId));

        /*new DownloadedTrackDaoHelper.GetDownloadedTrackFromRoomDatabase(mContext, mDownloadedTracks, position, Constants.ArrayOperation.SET)
                .execute(trackId);*/
        //mDownloadedTracks.set(position, null); // We'll fetch the database in onBindViewHolder()

        // mDownloadedTracks.set(position, OudUtils.getDownloadedTrack(mContext, trackId));


        //mHeartClickListener.add(position, heartClickListener);
    }

    public void removeTrack(int position) {
        //mTrackClickListener.remove(position);
        mIds.remove(position);
        mTrackImages.remove(position);
        mTrackNames.remove(position);
        mLikedTracks.remove(position);
        mDownloaded.remove(position);
        // mDownloadInfos.remove(position);
        //mDownloadedTracks.remove(position);
        //mHeartClickListener.remove(position);
    }

    public void swapTracks(int i, int j) {
        //Collections.swap(mTrackClickListener, i, j);
        Collections.swap(mIds, i, j);
        Collections.swap(mTrackImages, i, j);
        Collections.swap(mTrackNames, i, j);
        Collections.swap(mLikedTracks, i, j);
        Collections.swap(mDownloaded, i, j);
        //Collections.swap(mDownloadInfos, i, j);
        //Collections.swap(mDownloadedTracks, i, j);
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
                /*if (this.trackClickListener != null)
                    this.trackClickListener.onTrackClickListener(getAdapterPosition(), v);*/

                MediaPlayer mp = new MediaPlayer();

                try {
                    mp.setDataSource("/storage/emulated/0/Android/data/com.example.oud/cache/5e9b3d310f27b613b44fba2f-5e907db0a0645f558160760d-1587347166254.mp3");
                    mp.prepare();
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            mAvailableOffline.setOnClickListener(v -> {
                if (this.availableOfflineClickListener != null)
                    this.availableOfflineClickListener.onTrackClickListener(getAdapterPosition(), v);

                //DownloadInfo downloadInfo = mDownloadInfos.get(getAdapterPosition());
                if (mAvailableOffline.getAlpha() <= 0.5f)
                    return;

                mAvailableOffline.setAlpha(0.5f);

                if (mDownloaded.get(getAdapterPosition()) != null)
                    if (mDownloaded.get(getAdapterPosition()))
                        deleteDownloadedTrack();
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

            DownloadedTrack downloadedTrack = new DownloadedTrack();
            downloadedTrack.id = id;
            downloadedTrack.name = name;
            downloadedTrack.image = image;

            if (currentlyBeingDownloadedTracks == null)
                currentlyBeingDownloadedTracks = new LinkedList<>();

            currentlyBeingDownloadedTracks.add(downloadedTrack);

            //File file = new File(mContext.getExternalCacheDir().getAbsolutePath());
            File file = new File(mContext.getExternalCacheDir().getAbsolutePath());

            mProgressBarDownload.setVisibility(View.VISIBLE);

            String url = baseUrl + "tracks/" + id + "/download";

            DownloadService.enqueueDownload(mContext,
                    url,
                    loggedInUserId,
                    id,
                    image,
                    name);

            /*Pump.newRequest(baseUrl + "tracks/" + id + "/download")
                    //Pump.newRequest("https://server10.mp3quran.net/ajm/128/001.mp3")
                    .setId(id)
                    .forceReDownload(true)
                    .tag(loggedInUserId)
                    .listener(new DownloadListener() {
                        @Override
                        public void onFailed() {
                            super.onFailed();

                            currentlyBeingDownloadedTracks.remove(downloadedTrack);
                        }

                        @Override
                        public void onSuccess() {

                            currentlyBeingDownloadedTracks.remove(downloadedTrack);

                            super.onSuccess();
                            DownloadInfo downloadInfo = getDownloadInfo();
                            if (downloadInfo.getId().equals(id)) {

                                downloadedTrack.fileName = downloadInfo.getFilePath();

                                //mDownloadedTracks.set(pos, downloadedTrack);

                                new DownloadedTrackDaoHelper.InsertDownloadedTrackIntoRoomDatabase(mContext, currentlyBeingDownloadedTracks).execute(downloadedTrack);
                            }


                        }
                    })
                    .submit();*/

            /*List<DownloadInfo> downloadInfoList = Pump.getDownloadListByTag(loggedInUserId);
            for (DownloadInfo downloadInfo : downloadInfoList) {
                if (downloadInfo.getId().equals(id)) {
                    downloadInfo.setExtraData(TrackItemViewHolder.this);
                    // mDownloadInfos.set(getAdapterPosition(), downloadInfo);
                }
            }*/

            //mDownloadedTracks.set(getAdapterPosition(), d)


        }

        private void deleteDownloadedTrack() {
            String id = mIds.get(getAdapterPosition());
            // new DeleteTrackAsyncTask().execute(id, mAvailableOffline);
            new DeleteDownloadedTrackFromRoomDatabase(mContext, TrackItemViewHolder.this, getAdapterPosition(), mDownloaded).execute(id);
        }


    }

    /*private static class DeleteTrackAsyncTask extends AsyncTask<Object, Void, ImageButton> {

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
    }*/

    public static class DeleteDownloadedTrackFromRoomDatabase extends AsyncTask<String, Void, Void> {

        private WeakReference<Context> mContext;
        private WeakReference<TrackItemViewHolder> mHolder;

        private int mPosition;
        private WeakReference<ArrayList<Boolean>> mDownloaded;

        public DeleteDownloadedTrackFromRoomDatabase(Context context, TrackItemViewHolder trackItemViewHolder, int position, ArrayList<Boolean> downloaded) {
            this.mContext = new WeakReference<>(context);
            this.mHolder = new WeakReference<>(trackItemViewHolder);
            this.mPosition = position;
            this.mDownloaded = new WeakReference<>(downloaded);
        }

        @Override
        protected Void doInBackground(String... downloadedTrackIds) {
            DownloadedTracksDatabase downloadedTracksDatabase = OudUtils.getDownloadedTracksDatabase(mContext.get());

            DownloadedTrack downloadedTrack = downloadedTracksDatabase.downloadedTrackDao().getDownloadedTrack(downloadedTrackIds[0]);

            downloadedTracksDatabase.downloadedTrackDao().delete(downloadedTrackIds[0], downloadedTrack.userId);

            mDownloaded.get().set(mPosition, false);

            int trackCount = downloadedTracksDatabase.downloadedTrackDao().getNumOfTracksWithId(downloadedTrackIds[0]);
            if (trackCount > 1) // track exists for a different user
                return null;


            String filePath = downloadedTrack.fileName;
            File file = new File(filePath);

            if (file.exists()) {
                file.delete();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mHolder.get().mAvailableOffline.setAlpha(1.0f);
            mHolder.get().mAvailableOffline.setColorFilter(Color.WHITE);
        }
    }

    public interface OnTrackClickListener {
        void onTrackClickListener(int position, View view);
    }
}
