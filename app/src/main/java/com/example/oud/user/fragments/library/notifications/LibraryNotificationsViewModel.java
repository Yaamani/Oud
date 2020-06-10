package com.example.oud.user.fragments.library.notifications;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.Notification;
import com.example.oud.api.OudList;
import com.example.oud.user.fragments.library.LibrarySubFragmentViewModel;

import androidx.lifecycle.MutableLiveData;

public class LibraryNotificationsViewModel extends LibrarySubFragmentViewModel<LibraryNotificationsRepository, Notification> {

    public LibraryNotificationsViewModel() {
        super(LibraryNotificationsRepository.getInstance());
    }

    @Override
    protected MutableLiveData<OudList<Notification>> repoFetchItems(String token, int limit, int offset) {
        return mRepo.fetchNotifications(token, limit, offset);
    }

    @Override
    public void repoRemoveItem(String token, String id, ConnectionStatusListener undoUiAndUpdateLiveData) {
        mRepo.deleteNotification(token, id, undoUiAndUpdateLiveData);
    }
}
