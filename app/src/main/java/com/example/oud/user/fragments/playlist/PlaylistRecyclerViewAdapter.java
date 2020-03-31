package com.example.oud.user.fragments.playlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oud.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.TrackItemViewHolder> {

    private ArrayList<String> trackNames;

    public PlaylistRecyclerViewAdapter(ArrayList<String> trackNames) {
        this.trackNames = trackNames;
    }

    @NonNull
    @Override
    public TrackItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_playlist_track, parent, false);
        return new TrackItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackItemViewHolder holder, int position) {

        holder.textView.setText(trackNames.get(position));

    }

    @Override
    public int getItemCount() {
        return trackNames.size();
    }

    static class TrackItemViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        TrackItemViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.txt_track_playlist);
        }
    }
}
