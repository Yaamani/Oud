package com.example.oud.user.fragments.premium.database;

import android.content.Context;
import android.os.AsyncTask;

import com.example.oud.Constants;
import com.example.oud.OudUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;

public class DownloadedTrackDaoHelper {

    public static class GetDownloadedTrackFromRoomDatabase extends AsyncTask<String, Void, DownloadedTrack> {

        private WeakReference<Context> mContext;
        private WeakReference<ArrayList<DownloadedTrack>> mDownloadedTracksWeakReference;
        private int mPosition;
        private Constants.ArrayOperation arrayOperation;

        public GetDownloadedTrackFromRoomDatabase(Context context,
                                                  ArrayList<DownloadedTrack> downloadedTracks,
                                                  int position,
                                                  Constants.ArrayOperation arrayOperation) {
            this.mContext = new WeakReference<>(context);
            this.mDownloadedTracksWeakReference = new WeakReference<>(downloadedTracks);
            this.mPosition = position;
            this.arrayOperation = arrayOperation;
        }

        @Override
        protected DownloadedTrack doInBackground(String ... downloadedTrackIds) {
            DownloadedTracksDatabase downloadedTracksDatabase = OudUtils.getDownloadedTracksDatabase(mContext.get());

            return downloadedTracksDatabase.downloadedTrackDao().getDownloadedTrack(downloadedTrackIds[0]);
        }

        @Override
        protected void onPostExecute(DownloadedTrack downloadedTrack) {
            super.onPostExecute(downloadedTrack);
            if (arrayOperation == Constants.ArrayOperation.ADD)
                mDownloadedTracksWeakReference.get().add(mPosition, downloadedTrack);
            else if (arrayOperation == Constants.ArrayOperation.SET)
                mDownloadedTracksWeakReference.get().set(mPosition, downloadedTrack);

        }
    }

    public static class InsertDownloadedTrackIntoRoomDatabase extends AsyncTask<DownloadedTrack, Void, DownloadedTrack> {

        private WeakReference<Context> mContext;
        private WeakReference<LinkedList<DownloadedTrack>> currentlyBeingDownloadedTracks;

        public InsertDownloadedTrackIntoRoomDatabase(Context context, LinkedList<DownloadedTrack> currentlyBeingDownloadedTracks) {
            this.mContext = new WeakReference<>(context);
            this.currentlyBeingDownloadedTracks = new WeakReference<>(currentlyBeingDownloadedTracks);
        }

        @Override
        protected DownloadedTrack doInBackground(DownloadedTrack... downloadedTracks) {
            DownloadedTracksDatabase downloadedTracksDatabase = OudUtils.getDownloadedTracksDatabase(mContext.get());

            downloadedTracksDatabase.downloadedTrackDao().insert(downloadedTracks[0]);

            return downloadedTracks[0];
        }

        @Override
        protected void onPostExecute(DownloadedTrack downloadedTrack) {
            super.onPostExecute(downloadedTrack);
            int i = 0;
            for (DownloadedTrack current : currentlyBeingDownloadedTracks.get()) {
                if (current.id.equals(downloadedTrack.id)) {
                    currentlyBeingDownloadedTracks.get().remove(i);
                    break;
                }
                i++;
            }
        }
    }

    /*public static class DeleteDownloadedTrackFromRoomDatabase extends AsyncTask<String, Void, Void> {


        private WeakReference<Context> mContext;

        public DeleteDownloadedTrackFromRoomDatabase(Context context) {
            this.mContext = new WeakReference<>(context);
        }
        @Override
        protected Void doInBackground(String... downloadedTrackIds) {
            DownloadedTracksDatabase downloadedTracksDatabase = OudUtils.getDownloadedTracksDatabase(mContext.get());

            String filePath = downloadedTracksDatabase.downloadedTrackDao().getDownloadedTrack(downloadedTrackIds[0]).fileName;
            File file = new File(filePath);

            if (file.exists())
                file.delete();

            downloadedTracksDatabase.downloadedTrackDao().delete(downloadedTrackIds[0]);


            return null;
        }

    }*/
}
