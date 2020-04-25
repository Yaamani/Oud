package com.example.oud.user.fragments.premium;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.api.OudApi;

import java.io.File;
import java.util.LinkedList;

import androidx.annotation.NonNull;

public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();

    public static final int PROGRESS_UPDATE_DELAY = 200; //ms

    private String token;

    private BroadcastReceiver mDownloadCompleteReceiver;

    private LinkedList<TrackDownloadInfo> mTrackDownloadInfoList;
    private Handler mHandler;

    private LinkedList<DownloadListener> downloadListeners;

    private final IBinder mBinder = new DownloadServiceBinder();

    public DownloadService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeDownloadCompleteReceiver();
        registerReceiver(mDownloadCompleteReceiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        token = OudUtils.getToken(this);

        mHandler = new Handler();
        mHandler.post(progressRunnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
        TrackDownloadInfo trackDownloadInfo = constructTrackDownloadInfo(intent);

        if (mTrackDownloadInfoList == null)
            mTrackDownloadInfoList = new LinkedList<>();
        mTrackDownloadInfoList.add(trackDownloadInfo);


            DownloadManager.Request request = constructDownloadRequest(trackDownloadInfo);

            trackDownloadInfo.downloadId =
                    ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public static void enqueueDownload(@NonNull Context context,
                                       @NonNull String url,
                                       @NonNull String userId,
                                       @NonNull String trackId,
                                       @NonNull String trackImage,
                                       @NonNull String trackName) {

        Intent downloadIntent = new Intent(context, DownloadService.class);

        downloadIntent.putExtra(Constants.URL_KEY, url);
        downloadIntent.putExtra(Constants.USER_ID_KEY, userId);
        downloadIntent.putExtra(Constants.TRACK_ID_KEY, trackId);
        downloadIntent.putExtra(Constants.TRACK_IMAGE_KEY, trackImage);
        downloadIntent.putExtra(Constants.TRACK_NAME_KEY, trackName);

        context.startService(downloadIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d(TAG, "onTaskRemoved: called.");
        unregisterReceiver(mDownloadCompleteReceiver);
        stopSelf();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDownloadCompleteReceiver);
        stopSelf();
    }

    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            // Log.d(TAG, "progressRunnable: ");

            if (mTrackDownloadInfoList != null) {

                for (TrackDownloadInfo trackDownloadInfo : mTrackDownloadInfoList) {
                    if (trackDownloadInfo.downloadStatus != DownloadManager.STATUS_FAILED &
                            trackDownloadInfo.downloadStatus != DownloadManager.STATUS_SUCCESSFUL) {

                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById(trackDownloadInfo.downloadId);

                        DownloadManager dm =
                                ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE));

                        Cursor c = dm.query(query);
                        if (c.moveToFirst()) {
                            int sizeIndex = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                            int downloadedIndex = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                            long size = c.getInt(sizeIndex);
                            long downloaded = c.getInt(downloadedIndex);
                            int progress = 0;
                            if (size != -1) {
                                progress = (int) (downloaded * 100.0f / size);

                                for (DownloadListener downloadListener : downloadListeners) {
                                    downloadListener.onProgress(trackDownloadInfo, progress);
                                }
                            } else {
                                int statusIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                trackDownloadInfo.downloadStatus = c.getInt(statusIndex);
                            }

                        }

                    }
                }

            }
            mHandler.postDelayed(this, PROGRESS_UPDATE_DELAY);
        }
    };


    public void addDownloadListener(DownloadListener downloadListener) {
        if (downloadListeners == null)
            downloadListeners = new LinkedList<>();
        downloadListeners.add(downloadListener);
    }

    public void removeDownloadListener(DownloadListener downloadListener) {
        if (downloadListeners == null)
            downloadListeners = new LinkedList<>();
        downloadListeners.remove(downloadListener);
    }

    private TrackDownloadInfo constructTrackDownloadInfo(Intent downloadIntent) {
        Bundle intentBundle = downloadIntent.getExtras();

        TrackDownloadInfo trackDownloadInfo = new TrackDownloadInfo();
        trackDownloadInfo.url = intentBundle.getString(Constants.URL_KEY);
        trackDownloadInfo.userId = intentBundle.getString(Constants.USER_ID_KEY);
        trackDownloadInfo.trackId = intentBundle.getString(Constants.TRACK_ID_KEY);
        trackDownloadInfo.trackImage = intentBundle.getString(Constants.TRACK_IMAGE_KEY);
        trackDownloadInfo.trackName = intentBundle.getString(Constants.TRACK_NAME_KEY);

        return trackDownloadInfo;
    }

    private DownloadManager.Request constructDownloadRequest(TrackDownloadInfo trackDownloadInfo) {
        Uri downloadUri = Uri.parse(trackDownloadInfo.url);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);

        request.addRequestHeader(OudApi.AUTHORIZATION_HEADER, token);
        request.setTitle("Downloading " + trackDownloadInfo.trackName);

        String fileName = trackDownloadInfo.trackId + Constants.OFFLINE_TRACKS_EXTENSION;
        trackDownloadInfo.fileName = fileName;
        File file = new File(getExternalFilesDir(Constants.OFFLINE_TRACKS_DIR_NAME).getAbsolutePath() + File.separator + fileName);
        Uri destinationUri = Uri.fromFile(file);
        request.setDestinationUri(destinationUri);


        return request;
    }

    private void initializeDownloadCompleteReceiver() {
        mDownloadCompleteReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(
                            DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                    TrackDownloadInfo trackDownloadInfo = getTrackDownloadInfoWithDownloadId(downloadId);

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);

                    DownloadManager dm =
                            ((DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE));
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);

                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {

                            trackDownloadInfo.downloadStatus = DownloadManager.STATUS_SUCCESSFUL;

                            for (DownloadListener downloadListener : downloadListeners) {
                                downloadListener.onSuccess(trackDownloadInfo);
                            }

                        } else if (DownloadManager.STATUS_FAILED == c
                                .getInt(columnIndex)) {

                            trackDownloadInfo.downloadStatus = DownloadManager.STATUS_FAILED;


                            int columnReason = c.getColumnIndex(DownloadManager.COLUMN_REASON);
                            int reason = c.getInt(columnReason);

                            String failedReason = "";

                            switch(reason){
                                case DownloadManager.ERROR_CANNOT_RESUME:
                                    failedReason = "ERROR_CANNOT_RESUME";
                                    break;
                                case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                                    failedReason = "ERROR_DEVICE_NOT_FOUND";
                                    break;
                                case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                                    failedReason = "ERROR_FILE_ALREADY_EXISTS";
                                    break;
                                case DownloadManager.ERROR_FILE_ERROR:
                                    failedReason = "ERROR_FILE_ERROR";
                                    break;
                                case DownloadManager.ERROR_HTTP_DATA_ERROR:
                                    failedReason = "ERROR_HTTP_DATA_ERROR";
                                    break;
                                case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                                    failedReason = "ERROR_INSUFFICIENT_SPACE";
                                    break;
                                case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                                    failedReason = "ERROR_TOO_MANY_REDIRECTS";
                                    break;
                                case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                                    failedReason = "ERROR_UNHANDLED_HTTP_CODE";
                                    break;
                                case DownloadManager.ERROR_UNKNOWN:
                                    failedReason = "ERROR_UNKNOWN";
                                    break;
                            }

                            Log.d(TAG, "onReceive: Failure reason: " + reason + ", " + failedReason);

                            for (DownloadListener downloadListener : downloadListeners) {
                                downloadListener.onFailure(trackDownloadInfo, reason);
                            }
                        }
                    }
                }
            }
        };
    }

    private TrackDownloadInfo getTrackDownloadInfoWithDownloadId(long downloadId) {
        TrackDownloadInfo trackDownloadInfo = null;
        for (TrackDownloadInfo current : mTrackDownloadInfoList) {
            if (current.downloadId == downloadId) {
                trackDownloadInfo = current;
                break;
            }
        }
        return trackDownloadInfo;
    }

    public class DownloadServiceBinder extends Binder {

        public DownloadService getDownloadService(){
            return DownloadService.this;
        }

    }

    public static final class TrackDownloadInfo {

        private int downloadStatus = 0;

        private long downloadId;

        private String url;
        private String userId;
        private String trackId;
        private String trackImage;
        private String trackName;
        private String fileName;

        public String getUserId() {
            return userId;
        }

        public String getTrackId() {
            return trackId;
        }

        public String getTrackImage() {
            return trackImage;
        }

        public String getTrackName() {
            return trackName;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public interface DownloadListener {
        void onSuccess(TrackDownloadInfo trackDownloadInfo);
        void onProgress(TrackDownloadInfo trackDownloadInfo, int progress);
        void onFailure(TrackDownloadInfo trackDownloadInfo, int reason);
    }
}
