package com.example.oud.user.fragments.library.savedalbums;

import com.example.oud.connectionaware.ConnectionAwareRepository;


public class LibrarySavedAlbumsRepository extends ConnectionAwareRepository {

    private static final String TAG = LibrarySavedAlbumsRepository.class.getSimpleName();

    private static final LibrarySavedAlbumsRepository ourInstance = new LibrarySavedAlbumsRepository();

    private LibrarySavedAlbumsRepository() {}

    public static LibrarySavedAlbumsRepository getInstance() {
        return ourInstance;
    }



}
