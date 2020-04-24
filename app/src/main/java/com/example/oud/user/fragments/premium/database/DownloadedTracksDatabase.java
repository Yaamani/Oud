package com.example.oud.user.fragments.premium.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DownloadedTrack.class}, version = 1)
public abstract class DownloadedTracksDatabase extends RoomDatabase {

    public abstract DownloadedTrackDao downloadedTrackDao();

}