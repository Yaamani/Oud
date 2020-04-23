package com.example.oud;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class OptionsRecyclerViewAdapter extends RecyclerView.Adapter<OptionsRecyclerViewAdapter.OptionItemViewHolder> {

    private Context mContext;

    private int mContainerId;

    private ArrayList<Integer> mIcons;
    private ArrayList<String> mText;
    private ArrayList<Boolean> mSelectedItems;
    private ArrayList<View.OnClickListener> mClickListeners;


    public OptionsRecyclerViewAdapter(android.content.Context mContext,
                                      ArrayList<Integer> mIcons,
                                      ArrayList<String> mText,
                                      ArrayList<Boolean> selectedItems,
                                      ArrayList<View.OnClickListener> mClickListeners,
                                      @IdRes int mContainerId) {
        this.mContext = mContext;
        this.mIcons = mIcons;
        this.mText = mText;
        this.mSelectedItems = selectedItems;
        this.mClickListeners = mClickListeners;
        this.mContainerId = mContainerId;
    }

    @NonNull
    @Override
    public OptionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_options_item, parent, false);
        return new OptionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionItemViewHolder holder, int position) {


        Glide.with(mContext)
                .load(mIcons.get(position))
                .into(holder.mIcon);

        holder.mText.setText(mText.get(position));

        holder.itemView.setOnClickListener(v -> {
            mClickListeners.get(position).onClick(v);
            OptionsFragment.hideOptionsFragment((FragmentActivity) mContext, mContainerId);
        });

        /*holder.itemView.setClickable(false);
        holder.itemView.setFocusable(false);*/

        if (mSelectedItems.get(position)) {
            int tintColor = mContext.getResources().getColor(R.color.colorPrimary);
            holder.mIcon.setColorFilter(tintColor);
            holder.mText.setTextColor(tintColor);
        }


    }

    @Override
    public int getItemCount() {
        return mIcons.size();
    }

    static class OptionItemViewHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mText;

        public OptionItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.img_options_item_icon);
            mText = itemView.findViewById(R.id.txt_options_item);
        }
    }
}
