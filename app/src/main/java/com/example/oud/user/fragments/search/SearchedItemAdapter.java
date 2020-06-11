package com.example.oud.user.fragments.search;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.oud.OudUtils;
import com.example.oud.R;
import com.example.oud.api.Categories;
import com.example.oud.api.SearchedItems;

import java.util.ArrayList;

public class SearchedItemAdapter extends RecyclerView.Adapter<SearchedItemAdapter.SearchedViewHolder> {

    private ArrayList<SearchedItems> mSearchedItems;
    private Context mContext;
    private SearchedItemAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position/*,String Type,String id*/);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(SearchedItemAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public static class SearchedViewHolder extends RecyclerView.ViewHolder{

        public ImageView mItemIcon;
        public ImageView mDeleteIcon;
        public TextView mItemName;
        public TextView mItemType;
        public TextView mItemTypeStringOnly;


        public SearchedViewHolder(@NonNull View itemView, final SearchedItemAdapter.OnItemClickListener listener) {
            super(itemView);

            mItemIcon = itemView.findViewById(R.id.img_item);
            /*mDeleteIcon = itemView.findViewById(R.id.img_delete);*/

            mItemName = itemView.findViewById(R.id.txt_item_name);
            mItemType = itemView.findViewById(R.id.txt_item_type);

            mItemTypeStringOnly = itemView.findViewById(R.id.txt_string_only);


/*
            mDeleteIcon.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });
*/

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

        }
    }

    public SearchedItemAdapter(Context context, ArrayList<SearchedItems> searchedItems){

        mContext = context;
        mSearchedItems = searchedItems;

    }

    @NonNull
    @Override
    public SearchedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recent_searches,parent,false);
        SearchedViewHolder svh = new SearchedViewHolder(view,mListener);
        return  svh;

    }

    @Override
    public void onBindViewHolder(@NonNull SearchedViewHolder holder, int position) {

        SearchedItems currentItem = mSearchedItems.get(position);

        if(currentItem.isStringOnly()){

            holder.mItemName.setVisibility(View.GONE);
            holder.mItemIcon.setVisibility(View.GONE);
            /*holder.mDeleteIcon.setVisibility(View.GONE);*/
            holder.mItemType.setVisibility(View.GONE);
            holder.mItemTypeStringOnly.setVisibility(View.VISIBLE);

            holder.mItemTypeStringOnly.setText(currentItem.getItemType());

        }
        else {
            holder.mItemTypeStringOnly.setVisibility(View.GONE);

            DrawableCrossFadeFactory factory =
                    new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

            String fullUrl = OudUtils.convertImageToFullUrl(currentItem.getImage());

            OudUtils.glideBuilder(mContext, fullUrl).placeholder(R.drawable.ic_oud_loading)
                    .transition(DrawableTransitionOptions.withCrossFade(factory))
                    .into(holder.mItemIcon);

            holder.mItemName.setText(currentItem.getItemName());
            holder.mItemType.setText(currentItem.getItemType());
        }

    }

    @Override
    public int getItemCount() {
        return mSearchedItems.size();
    }
}
