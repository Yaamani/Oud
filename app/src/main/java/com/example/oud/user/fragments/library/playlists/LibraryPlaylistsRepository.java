package com.example.oud.user.fragments.library.playlists;

import com.example.oud.connectionaware.ConnectionAwareRepository;


public class LibraryPlaylistsRepository extends ConnectionAwareRepository {

    private static final String TAG = LibraryPlaylistsRepository.class.getSimpleName();

    private static final LibraryPlaylistsRepository ourInstance = new LibraryPlaylistsRepository();

    private LibraryPlaylistsRepository() {}

    public static LibraryPlaylistsRepository getInstance() {
        return ourInstance;
    }



}
