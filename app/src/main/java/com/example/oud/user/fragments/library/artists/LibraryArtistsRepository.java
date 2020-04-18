package com.example.oud.user.fragments.library.artists;

import com.example.oud.connectionaware.ConnectionAwareRepository;


public class LibraryArtistsRepository extends ConnectionAwareRepository {

    private static final String TAG = LibraryArtistsRepository.class.getSimpleName();

    private static final LibraryArtistsRepository ourInstance = new LibraryArtistsRepository();

    private LibraryArtistsRepository() {}

    public static LibraryArtistsRepository getInstance() {
        return ourInstance;
    }



}
