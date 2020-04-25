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

    @Query("SELECT * FROM downloaded_track WHERE userId = :userId")
    List<DownloadedTrack> getAllWithUserId(String userId);

    @Query("SELECT COUNT(*) FROM downloaded_track WHERE id = :trackId")
    int getNumOfTracksWithId(String trackId);

    @Query("SELECT * FROM downloaded_track WHERE id = :trackId")
    DownloadedTrack getDownloadedTrack(String trackId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DownloadedTrack downloadedTrack);

    @Delete()
    void delete(DownloadedTrack downloadedTrack);

    @Query("DELETE FROM downloaded_track WHERE id = :trackId AND userId = :userId")
    void delete(String trackId, String userId);
}
