package com.example.oud.user.fragments.premium.database;

import android.net.Uri;

import org.jetbrains.annotations.NotNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "downloaded_track")
public class DownloadedTrack {

    @PrimaryKey
    @NotNull
    public String id;

    @ColumnInfo
    public String name;

    @ColumnInfo
    public String image;

    @ColumnInfo
    public String filePath;

    /*@ColumnInfo
    public boolean liked;*/


}
