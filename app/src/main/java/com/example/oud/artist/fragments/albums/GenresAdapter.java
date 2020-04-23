package com.example.oud.artist.fragments.albums;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oud.R;

import java.util.ArrayList;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.GenresViewHolder> {
    ArrayList<String> genresNames=new ArrayList<>();
    ArrayList<String> genresIds= new ArrayList<>();
    ArrayList<Boolean> genresAdded=new ArrayList<>();

    Context context;

    public GenresAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public GenresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_genres, parent, false);
        return new GenresViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GenresViewHolder holder, int position) {
        holder.notAdded.setVisibility(View.VISIBLE);
        holder.added.setVisibility(View.INVISIBLE);

        holder.notAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genresAdded.set(position,true);
                holder.notAdded.setVisibility(View.INVISIBLE);
                holder.added.setVisibility(View.VISIBLE);
            }
        });

        holder.added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genresAdded.set(position,false);
                holder.added.setVisibility(View.INVISIBLE);
                holder.notAdded.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return genresIds.size();
    }

    public ArrayList<String> getChosenGenres(){
        ArrayList<String> chosenGenres = new ArrayList<>();
        for(int i =0 ;i<genresIds.size();i++){
            if(genresAdded.get(i))
                chosenGenres.add(genresIds.get(i));

        }
        return chosenGenres;
    }

    public void addItem (String genreName,String genreId){
        genresIds.add(genreId);
        genresNames.add(genreName);
        genresAdded.add(false);
    }

    public class GenresViewHolder extends RecyclerView.ViewHolder{
        Button notAdded;
        Button added;
        public GenresViewHolder(View itemView){
            super(itemView);
            notAdded = itemView.findViewById(R.id.btn_genre_not_added);
            added = itemView.findViewById(R.id.btn_genre_added);

        }

    }

}
