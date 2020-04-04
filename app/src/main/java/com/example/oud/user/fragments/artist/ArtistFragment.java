package com.example.oud.user.fragments.artist;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.glide.transformations.BlurTransformation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.oud.Constants;
import com.example.oud.R;
import com.example.oud.connectionaware.ConnectionAwareFragment;
import com.example.oud.connectionaware.ConnectionAwareViewModel;
import com.example.oud.user.fragments.playlist.PlaylistFragment;

public class ArtistFragment extends ConnectionAwareFragment<ArtistViewModel> {

    private String artistId;

    private TextView mTextViewArtistName;
    private ImageView mImageViewArtist;
    private ImageView mImageViewArtistBlurred;

    private RecyclerView mRecyclerViewPopularSongs;
    private RecyclerView mRecyclerViewAlbums;
    private RecyclerView mRecyclerViewSimilarArtists;

    private TextView mTextViewNoSongsToShow;
    private TextView mTextViewNoAlbumsToShow;
    private TextView mTextViewNoArtistsToShow;

    private TextView mTextViewBio;


    public ArtistFragment() {
        super(ArtistViewModel.class,
                R.layout.fragment_artist,
                R.id.progress_artist,
                null);
    }

    public static ArtistFragment newInstance(String artistId) {
        ArtistFragment artistFragment = new ArtistFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ARTIST_ID_KEY, artistId);
        artistFragment.setArguments(bundle);
        return artistFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleArgs();

        mTextViewArtistName = view.findViewById(R.id.txt_artist_name);
        mImageViewArtist = view.findViewById(R.id.img_artist);
        mImageViewArtistBlurred = view.findViewById(R.id.img_artist_blurred);

        mRecyclerViewPopularSongs = view.findViewById(R.id.recycler_view_artist_popular_songs);
        mRecyclerViewAlbums = view.findViewById(R.id.recycler_view_artist_albums);
        mRecyclerViewSimilarArtists = view.findViewById(R.id.recycler_view_artist_similar_artists);

        mTextViewNoSongsToShow = view.findViewById(R.id.txt_artist_no_songs_to_show);
        mTextViewNoAlbumsToShow = view.findViewById(R.id.txt_artist_no_albums_to_show);
        mTextViewNoArtistsToShow = view.findViewById(R.id.txt_artist_no_artists_to_show);

        mTextViewBio = view.findViewById(R.id.txt_artist_about_bio);

        handleData();

    }

    private void handleArgs() {
        Bundle args = getArguments();
        if (args != null) {
            artistId = args.getString(Constants.ARTIST_ID_KEY);
        } else {
            String fragName = ArtistFragment.class.getSimpleName();
            throw new RuntimeException("Instead of calling new " + fragName + "()" +
                    ", call " + fragName + ".newInstance(id)" +
                    " to pass the arguments to the fragment. Or you can use " + fragName + ".setArguments(" + fragName + ".myArgs()).");
        }
    }

    private void handleData() {
        mViewModel.getArtistMutableLiveData(artistId).observe(getViewLifecycleOwner(), artist -> {


            mTextViewArtistName.setText(artist.getName());
            Glide.with(getContext())
                    .load(artist.getImages().get(0))
                    .placeholder(R.drawable.ic_oud_loading)
                    .into(mImageViewArtist);
            new RequestOptions();
            Glide.with(getContext())
                    .load(artist.getImages().get(0))
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                    .placeholder(R.drawable.ic_oud_loading)
                    .into(mImageViewArtistBlurred);





            mTextViewBio.setText(artist.getBio());

        });
    }
}
