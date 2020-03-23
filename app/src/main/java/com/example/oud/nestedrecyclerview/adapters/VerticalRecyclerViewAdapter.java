package com.example.oud.nestedrecyclerview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.nestedrecyclerview.decorations.HorizontalSpaceDecoration;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.TeamViewHolder> {

    private Context mContext;

    private ArrayList<String> mIcons;
    private ArrayList<String> mTitles;
    private ArrayList<HorizontalRecyclerViewAdapter> mInnerItemAdapters;

    private HorizontalSpaceDecoration horizontalSpaceDecoration;
    private RecyclerView.RecycledViewPool sharedPool;

    public VerticalRecyclerViewAdapter(Context mContext, ArrayList<String> mIcons, ArrayList<String> mTitles, ArrayList<HorizontalRecyclerViewAdapter> mInnerItemAdapters, int horizontalRecyclerViewItemCount) {
        this.mContext = mContext;
        this.mIcons = mIcons;
        this.mTitles = mTitles;
        this.mInnerItemAdapters = mInnerItemAdapters;

        horizontalSpaceDecoration = new HorizontalSpaceDecoration(mContext.getResources(),
                R.dimen.item_margin,
                horizontalRecyclerViewItemCount);
        sharedPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outer, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        //holder.mTeamLogo.setImageDrawable(mLogos.get(position));
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        Glide.with(mContext)
                .load(mIcons.get(position))
                .placeholder(R.drawable.ic_loading)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(holder.mIcon);

        holder.mTitle.setText(mTitles.get(position));

        handleHorizontalRecyclerView(holder.mInnerRecyclerView, position);
        //holder.mTeamPlayers.setAdapter(players.get(position));
    }

    @Override
    public int getItemCount() {
        return mIcons.size();
    }

    private void handleHorizontalRecyclerView(RecyclerView recyclerView, int position) {
        //RecyclerView recyclerView = findViewById(R.id.single_team_recycler_view);
        if (recyclerView.getLayoutManager() == null)
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                    RecyclerView.HORIZONTAL,
                    false));

        if (recyclerView.getRecycledViewPool() != sharedPool)
            recyclerView.setRecycledViewPool(sharedPool);

        if (recyclerView.getItemDecorationCount() == 0)
            recyclerView.addItemDecoration(horizontalSpaceDecoration);

        /*adapter = new HorizontalRecyclerViewAdapter(
                liverpoolBitmaps, liverpoolPlayerNames, liverpoolPlayerPositions);*/
        recyclerView.setAdapter(mInnerItemAdapters.get(position));

    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon;
        private TextView mTitle;
        private RecyclerView mInnerRecyclerView;


        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.image_item_outer_icon);
            mTitle = itemView.findViewById(R.id.txt_item_outer_title);
            mInnerRecyclerView = itemView.findViewById(R.id.team_player_recycler_view);
        }
    }
}
