package com.example.oud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProfilePlaylistRecyclerViewAdapter extends RecyclerView.Adapter<ProfilePlaylistRecyclerViewAdapter.PlaylistViewHolder> {

    private ArrayList<String> playlistNames = new ArrayList<>();
    private ArrayList<String> playListImagesUrls = new ArrayList<>();
    private ArrayList<String> playlistId = new ArrayList<>();

    private Context context;

    public ProfilePlaylistRecyclerViewAdapter(Context context,ArrayList<String> playlistNames, ArrayList<String> playListImagesLinks, ArrayList<String> playlistId) {
        this.playlistNames = playlistNames;
        this.playListImagesUrls = playListImagesLinks;
        this.playlistId = playlistId;
        this.context = context;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_profile,parent,false);
        PlaylistViewHolder viewHolder = new PlaylistViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(playListImagesUrls.get(position)).into(holder.playlistItemImageView);
        holder.playlistItemTextView.setText(playlistNames.get(position));

    }

    @Override
    public int getItemCount() {
        return playListImagesUrls.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder{
        ImageView playlistItemImageView;
        TextView playlistItemTextView;
        RelativeLayout playlistParentRelativeLayout;
        public PlaylistViewHolder(View viewHolder){
            super(viewHolder);
            playlistItemImageView= viewHolder.findViewById(R.id.image_item_playlist);
            playlistItemTextView = viewHolder.findViewById(R.id.text_item_playlist_name);
            playlistParentRelativeLayout = viewHolder.findViewById(R.id.parent_item_playlist);
        }

    }
}
