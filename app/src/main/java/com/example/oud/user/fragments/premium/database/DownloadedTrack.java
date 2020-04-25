package com.example.oud.user.fragments.premium.database;

import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "downloaded_track", primaryKeys = {"id", "userId"})
public class DownloadedTrack {

    @ColumnInfo
    @NotNull
    public String id;

    @ColumnInfo
    @NonNull
    public String userId;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String image;

    @ColumnInfo
    public String fileName;

    /*@ColumnInfo
    public boolean liked;*/


}
