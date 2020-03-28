package com.example.oud.user.fragments.home.nestedrecyclerview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.R;
import com.example.oud.user.fragments.home.nestedrecyclerview.decorations.HorizontalSpaceDecoration;

import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VerticalRecyclerViewAdapter extends RecyclerView.Adapter<VerticalRecyclerViewAdapter.OuterItemViewHolder> {

    private Context mContext;

    @IdRes
    private ArrayList<Integer> icons;
    private ArrayList<String> titles;
    private ArrayList<HorizontalRecyclerViewAdapter> innerItemAdapters;

    private HorizontalSpaceDecoration horizontalSpaceDecoration;
    private RecyclerView.RecycledViewPool sharedPool;

    public VerticalRecyclerViewAdapter(Context mContext) {
        this(mContext, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public VerticalRecyclerViewAdapter(Context mContext, ArrayList<Integer> icons, ArrayList<String> titles, ArrayList<HorizontalRecyclerViewAdapter> innerItemAdapters) {
        this.mContext = mContext;
        this.icons = icons;
        this.titles = titles;
        this.innerItemAdapters = innerItemAdapters;

        horizontalSpaceDecoration = new HorizontalSpaceDecoration(mContext.getResources(),
                R.dimen.item_margin);
        sharedPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public OuterItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outer, parent, false);
        return new OuterItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OuterItemViewHolder holder, int position) {

        //holder.mTeamLogo.setImageDrawable(mLogos.get(position));
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        String iconTagPrefix = mContext.getResources().getString(R.string.tag_home_outer_item_icon);
        holder.mTitle.setTag(iconTagPrefix + position);
        Glide.with(mContext)
                .load(icons.get(position))
                //.placeholder(R.drawable.ic_loading)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(holder.mIcon);


        String titleTagPrefix = mContext.getResources().getString(R.string.tag_home_outer_item_title);
        holder.mTitle.setTag(titleTagPrefix + position);
        holder.mTitle.setText(titles.get(position));

        String recyclerViewTagPrefix = mContext.getResources().getString(R.string.tag_home_outer_item_recycler_view);
        holder.mInnerRecyclerView.setTag(recyclerViewTagPrefix + position);
        handleHorizontalRecyclerView(holder.mInnerRecyclerView, position);
        //holder.mTeamPlayers.setAdapter(players.get(position));
    }

    @Override
    public int getItemCount() {
        return icons.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull OuterItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        //holder.mInnerRecyclerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
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
        recyclerView.setAdapter(innerItemAdapters.get(position));

    }

    public static class OuterItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIcon;
        private TextView mTitle;
        private RecyclerView mInnerRecyclerView;


        public OuterItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.image_item_outer_icon);
            mTitle = itemView.findViewById(R.id.txt_item_outer_title);
            mInnerRecyclerView = itemView.findViewById(R.id.recycler_view_item_outer);
        }
    }

    public ArrayList<Integer> getIcons() {
        return icons;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public ArrayList<HorizontalRecyclerViewAdapter> getInnerItemAdapters() {
        return innerItemAdapters;
    }
}
