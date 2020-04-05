package com.example.oud.user.fragments.profile;

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
import com.example.oud.R;

import java.util.ArrayList;

public class ProfileFollowersRecyclerViewAdapter extends RecyclerView.Adapter<ProfileFollowersRecyclerViewAdapter.FollowersViewHolder> {


    private ArrayList<String> followerNames = new ArrayList<>();
    private ArrayList<String> followerImagesUrls = new ArrayList<>();
    private ArrayList<String> followerIds = new ArrayList<>();
    private ArrayList<String> followerTypes = new ArrayList<>();


    private Context context;


    public ProfileFollowersRecyclerViewAdapter(ArrayList<String> followerNames, ArrayList<String> followerImagesUrls, ArrayList<String> followerIds, Context context) {
        this.followerNames = followerNames;
        this.followerImagesUrls = followerImagesUrls;
        this.followerIds = followerIds;
        this.context = context;
    }

    @NonNull
    @Override
    public ProfileFollowersRecyclerViewAdapter.FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_profile,parent,false);
        ProfileFollowersRecyclerViewAdapter.FollowersViewHolder viewHolder = new ProfileFollowersRecyclerViewAdapter.FollowersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFollowersRecyclerViewAdapter.FollowersViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(followerImagesUrls.get(position)).into(holder.followerItemImageView);
        holder.followerItemTextView.setText(followerNames.get(position));

    }

    @Override
    public int getItemCount() {
        return followerImagesUrls.size();
    }







    public class FollowersViewHolder extends RecyclerView.ViewHolder{
        ImageView followerItemImageView;
        TextView followerItemTextView;
        RelativeLayout followerParentRelativeLayout;
        public FollowersViewHolder(View viewHolder){
            super(viewHolder);
            followerItemImageView= viewHolder.findViewById(R.id.image_item_follower_profile);
            followerItemTextView = viewHolder.findViewById(R.id.text_item_follower_profile_name);
            followerParentRelativeLayout = viewHolder.findViewById(R.id.parent_item_follower_profile);
        }

    }
}
