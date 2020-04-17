package com.example.oud.user.fragments.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oud.ConnectionStatusListener;
import com.example.oud.Constants;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.user.fragments.artist.ArtistFragment;
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

    ProfilePlaylistsViewModel mViewModel;
    LifecycleOwner lifecycleOwner;

    public ProfilePlaylistRecyclerViewAdapter(Context context,ArrayList<String> playlistNames, ArrayList<String> playListImagesLinks, ArrayList<String> playlistId,String userId,LifecycleOwner lifecycleOwner) {
        this.playlistNames = playlistNames;
        this.playListImagesUrls = playListImagesLinks;
        this.playlistId = playlistId;
        this.context = context;
        this.userId = userId;
        this.lifecycleOwner = lifecycleOwner;

        mViewModel = new ViewModelProvider((FragmentActivity)context).get(ProfilePlaylistsViewModel.class);
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
        holder.playlistParentConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaylistFragment.show((FragmentActivity)context,R.id.nav_host_fragment,userId, Constants.PlaylistFragmentType.PLAYLIST,playlistId.get(position));
            }
        });

        MutableLiveData<Boolean> isFollowed;
        holder.followButton.setVisibility(View.VISIBLE);
        holder.unFollowButton.setVisibility(View.INVISIBLE);


        isFollowed = mViewModel.checkIfIFollowThisPlaylist(OudUtils.getToken(context),playlistId.get(position),OudUtils.getUserId(context));

        isFollowed.observe(lifecycleOwner, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFollowed) {
                if(isFollowed){
                    holder.followButton.setVisibility(View.VISIBLE);
                    holder.unFollowButton.setVisibility(View.INVISIBLE);
                }else {
                    holder.followButton.setVisibility(View.INVISIBLE);
                    holder.unFollowButton.setVisibility(View.VISIBLE);
                }
                isFollowed = null;

            }
        });


        ConnectionStatusListener followConnectionStatusListener = new ConnectionStatusListener() {
            @Override
            public void onConnectionSuccess() {
                holder.unFollowButton.setEnabled(true);
            }

            @Override
            public void onConnectionFailure() {
                holder.unFollowButton.setVisibility(View.INVISIBLE);
                holder.followButton.setVisibility(View.VISIBLE);
                holder.unFollowButton.setEnabled(true);
            }
        };

        holder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.unFollowButton.setVisibility(View.VISIBLE);
                holder.unFollowButton.setEnabled(false);
                holder.followButton.setVisibility(View.INVISIBLE);
                mViewModel.followPlaylist(OudUtils.getToken(context),playlistId.get(position),followConnectionStatusListener);

            }
        });

        ConnectionStatusListener unFollowConnectionStatusListener = new ConnectionStatusListener() {
            @Override
            public void onConnectionSuccess() {
                holder.followButton.setEnabled(true);
            }

            @Override
            public void onConnectionFailure() {
                holder.followButton.setVisibility(View.INVISIBLE);
                holder.unFollowButton.setVisibility(View.VISIBLE);
                holder.followButton.setEnabled(true);
            }
        };

        holder.unFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.followButton.setVisibility(View.VISIBLE);
                holder.followButton.setEnabled(false);
                holder.unFollowButton.setVisibility(View.INVISIBLE);
                mViewModel.unFollowPlaylist(OudUtils.getToken(context),playlistId.get(position),unFollowConnectionStatusListener);

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
        ConstraintLayout playlistParentConstraintLayout;
        ImageButton followButton;
        ImageButton unFollowButton;
        public PlaylistViewHolder(View viewHolder){
            super(viewHolder);
            playlistItemImageView= viewHolder.findViewById(R.id.image_item_playlist);
            playlistItemTextView = viewHolder.findViewById(R.id.text_item_playlist_name);
            playlistParentConstraintLayout = viewHolder.findViewById(R.id.parent_item_playlist);
            followButton = viewHolder.findViewById(R.id.icon_item_playlist_follow);
            unFollowButton = viewHolder.findViewById(R.id.icon_item_playlist_un_follow);

            followButton.setVisibility(View.VISIBLE);
            unFollowButton.setVisibility(View.INVISIBLE);
        }

    }
}
