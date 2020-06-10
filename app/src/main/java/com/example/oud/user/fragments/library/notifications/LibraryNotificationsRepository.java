package com.example.oud.user.fragments.library.notifications;

import android.util.Log;

import com.example.oud.ConnectionStatusListener;
import com.example.oud.api.Notification;
import com.example.oud.api.OudList;
import com.example.oud.connectionaware.ConnectionAwareRepository;
import com.example.oud.connectionaware.FailureSuccessHandledCallback;

import androidx.lifecycle.MutableLiveData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class LibraryNotificationsRepository extends ConnectionAwareRepository {

    private static final String TAG = LibraryNotificationsRepository.class.getSimpleName();

    private static final LibraryNotificationsRepository ourInstance = new LibraryNotificationsRepository();

    private LibraryNotificationsRepository() {}

    public static LibraryNotificationsRepository getInstance() {
        return ourInstance;
    }





    public MutableLiveData<OudList<Notification>> fetchNotifications(String token, Integer limit, Integer offset) {
        MutableLiveData<OudList<Notification>> notificationsLiveData = new MutableLiveData<>();


        Call<OudList<Notification>> notificationsCall = oudApi.getNotificationHistory(token, limit, offset);
        addCall(notificationsCall).enqueue(new FailureSuccessHandledCallback<OudList<Notification>>(this) {
            @Override
            public void onResponse(Call<OudList<Notification>> call, Response<OudList<Notification>> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }

                notificationsLiveData.setValue(response.body());
            }
        });

        return notificationsLiveData;
    }


    public void deleteNotification(String token, String notificationId, ConnectionStatusListener undoUiAndUpdateLiveData) {

        Call<ResponseBody> deleteNotificationCall = oudApi.deleteNotification(token, notificationId);
        addCall(deleteNotificationCall).enqueue(new FailureSuccessHandledCallback<ResponseBody>(this, undoUiAndUpdateLiveData) {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e(TAG, "onResponse: " + response.code());
                    return;
                }
            }
        });

    }
}
