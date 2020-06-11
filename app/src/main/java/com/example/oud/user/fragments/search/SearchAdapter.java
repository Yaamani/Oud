package com.example.oud.user.fragments.search;

import android.content.Context;
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

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private ArrayList<Categories> mCategoriesItems;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class SearchViewHolder extends RecyclerView.ViewHolder{

        public ImageView mCategoryIcon1;
        public ImageView mCategoryIcon2;

        public TextView mCategoryName1;
        public TextView mCategoryName2;


        public SearchViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mCategoryIcon1 = itemView.findViewById(R.id.category_id);
            mCategoryIcon2 = itemView.findViewById(R.id.category_id2);
            mCategoryName1 = itemView.findViewById(R.id.text_category_name);
            mCategoryName2 = itemView.findViewById(R.id.text_category_name2);

            mCategoryIcon1.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });

            mCategoryIcon2.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position+1);
                    }
                }
            });

        }
    }

    public SearchAdapter(Context context, ArrayList<Categories> categoriesItems){

        mContext = context;
        mCategoriesItems = categoriesItems;

    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_for_search,parent,false);
        SearchViewHolder svh = new SearchViewHolder(view,mListener);
        return  svh;

    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {


        Categories currentItem = mCategoriesItems.get(position);
        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

        String fullUrl = OudUtils.convertImageToFullUrl(currentItem.getCategoryIcon());

        OudUtils.glideBuilder(mContext,fullUrl).placeholder(R.drawable.ic_oud_loading)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(holder.mCategoryIcon1);

        String fullUrl2 = OudUtils.convertImageToFullUrl(currentItem.getCategoryIcon2());

        OudUtils.glideBuilder(mContext,fullUrl2).placeholder(R.drawable.ic_oud_loading)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(holder.mCategoryIcon2);


        holder.mCategoryName1.setText(currentItem.getCategoryName());
        holder.mCategoryName2.setText(currentItem.getCategoryName2());


    }

    @Override
    public int getItemCount() {
        return mCategoriesItems.size();
    }
}
