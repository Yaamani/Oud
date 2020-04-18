package com.example.oud.user.fragments.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.oud.ConnectionStatusListener;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.user.fragments.artist.ArtistFragment;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ProfileFollowersRecyclerViewAdapter extends RecyclerView.Adapter<ProfileFollowersRecyclerViewAdapter.FollowersViewHolder> {


    private String myId;
    private ArrayList<String> followerNames = new ArrayList<>();
    private ArrayList<String> followerImagesUrls = new ArrayList<>();
    private ArrayList<String> followerIds = new ArrayList<>();
    private ArrayList<String> followerTypes = new ArrayList<>();

    private String userId;

    private LifecycleOwner lifecycleOwner;

    private Context context;

    ProfileFollowersViewModel mViewModel;

    public ProfileFollowersRecyclerViewAdapter(Context context , ArrayList<String> followerNames, ArrayList<String> followerImagesUrls, ArrayList<String> followerIds, ArrayList<String> followerTypes,LifecycleOwner lifecycleOwner) {
        this.followerNames = followerNames;
        this.followerImagesUrls = followerImagesUrls;
        this.followerIds = followerIds;
        this.followerTypes = followerTypes;
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;

        mViewModel = new ViewModelProvider((FragmentActivity)context).get(ProfileFollowersViewModel.class);

        myId = OudUtils.getUserId(context);

    }

    @NonNull
    @Override
    public FollowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower_profile,parent,false);
        FollowersViewHolder viewHolder = new ProfileFollowersRecyclerViewAdapter.FollowersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileFollowersRecyclerViewAdapter.FollowersViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(followerImagesUrls.get(position)).into(holder.followerItemImageView);
        holder.followerItemTextView.setText(followerNames.get(position));
        holder.followerParentConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(followerTypes.get(position)=="user"){
                    ProfileFragment.show((FragmentActivity)context,R.id.nav_host_fragment,followerIds.get(position));
                }
                else
                    ArtistFragment.show((FragmentActivity)context,R.id.nav_host_fragment,followerIds.get(position));
            }
        });

        MutableLiveData<Boolean> isFollowed;
        holder.followButton.setVisibility(View.VISIBLE);
        holder.unFollowButton.setVisibility(View.INVISIBLE);

        if(followerTypes.get(position).equals("user")){
            isFollowed = mViewModel.checkIfIFollowThisUser(OudUtils.getToken(context),followerIds.get(position));
        }else {
            isFollowed = mViewModel.checkIfIFollowThisArtist(OudUtils.getToken(context),followerIds.get(position));
        }

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

            }
        });

        holder.followerParentConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(followerTypes.get(position)=="user"){
                    ProfileFragment.show((FragmentActivity)context,R.id.nav_host_fragment,followerIds.get(position));
                }
                else
                    ArtistFragment.show((FragmentActivity)context,R.id.nav_host_fragment,followerIds.get(position), OudUtils.getUserId(context));
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
                if(followerTypes.get(position).equals("user")){
                    mViewModel.followUser(OudUtils.getToken(context),followerIds.get(position),followConnectionStatusListener);
                }
                else
                    mViewModel.followArtist(OudUtils.getToken(context),followerIds.get(position),followConnectionStatusListener);
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
                if(followerTypes.get(position).equals("user")){
                    mViewModel.unFollowUser(OudUtils.getToken(context),followerIds.get(position),unFollowConnectionStatusListener);
                }
                else
                    mViewModel.unFollowArtist(OudUtils.getToken(context),followerIds.get(position),unFollowConnectionStatusListener);
            }
        });


        if(followerIds.get(position).equals(myId)){
            holder.followButton.setVisibility(View.INVISIBLE);
            holder.unFollowButton.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return followerImagesUrls.size();
    }







    public class FollowersViewHolder extends RecyclerView.ViewHolder{
        ImageView followerItemImageView;
        TextView followerItemTextView;
        ConstraintLayout followerParentConstraintLayout;
        Button followButton;
        Button unFollowButton;

        public FollowersViewHolder(View viewHolder){
            super(viewHolder);
            followerItemImageView= viewHolder.findViewById(R.id.image_item_follower_profile);
            followerItemTextView = viewHolder.findViewById(R.id.text_item_follower_profile_name);
            followerParentConstraintLayout = viewHolder.findViewById(R.id.parent_item_follower_profile);
            followButton = viewHolder.findViewById(R.id.btn_item_profile_user_or_artist_follow);
            unFollowButton = viewHolder.findViewById(R.id.btn_item_profile_user_or_artist_un_follow);

        }

    }
}
