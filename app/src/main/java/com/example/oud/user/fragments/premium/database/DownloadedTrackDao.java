package com.example.oud.user.fragments.premium.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface DownloadedTrackDao {

    @Query("SELECT * FROM downloaded_track")
    List<DownloadedTrack> getAll();

    @Query("SELECT * FROM downloaded_track WHERE id = :trackId")
    DownloadedTrack getDownloadedTrack(String trackId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DownloadedTrack downloadedTrack);

    @Delete()
    void delete(DownloadedTrack downloadedTrack);

    @Query("DELETE FROM downloaded_track WHERE id = :trackId")
    void delete(String trackId);
}
