package com.example.oud.user.fragments.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.user.fragments.playlist.PlaylistFragment;

import java.util.ArrayList;

public class ProfilePlaylistRecyclerViewAdapter extends RecyclerView.Adapter<ProfilePlaylistRecyclerViewAdapter.PlaylistViewHolder> {

    public void setPlaylistNames(ArrayList<String> playlistNames) {
        this.playlistNames = playlistNames;
    }

    public void setPlayListImagesUrls(ArrayList<String> playListImagesUrls) {
        this.playListImagesUrls = playListImagesUrls;
    }

    public void setPlaylistId(ArrayList<String> playlistId) {
        this.playlistId = playlistId;
    }

    private ArrayList<String> playlistNames = new ArrayList<>();
    private ArrayList<String> playListImagesUrls = new ArrayList<>();
    private ArrayList<String> playlistId = new ArrayList<>();

    String userId;
    private Context context;

    public ProfilePlaylistRecyclerViewAdapter(Context context,ArrayList<String> playlistNames, ArrayList<String> playListImagesLinks, ArrayList<String> playlistId,String userId) {
        this.playlistNames = playlistNames;
        this.playListImagesUrls = playListImagesLinks;
        this.playlistId = playlistId;
        this.context = context;
        this.userId = userId;
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
        holder.playlistParentRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistFragment fragment = PlaylistFragment.newInstance(userId, Constants.PlaylistFragmentType.PLAYLIST,playlistId.get(position));
                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment,fragment)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return playListImagesUrls.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder{
        ImageView playlistItemImageView;
        TextView playlistItemTextView;
        RelativeLayout playlistParentRelativeLayout;
        String id;
        public PlaylistViewHolder(View viewHolder){
            super(viewHolder);
            playlistItemImageView= viewHolder.findViewById(R.id.image_item_playlist);
            playlistItemTextView = viewHolder.findViewById(R.id.text_item_playlist_name);
            playlistParentRelativeLayout = viewHolder.findViewById(R.id.parent_item_playlist);

        }

    }
}