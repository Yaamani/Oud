package com.example.oud.user.fragments.premium;

import com.huxq17.download.core.connection.DownloadConnection;
import com.huxq17.download.core.connection.OkHttpDownloadConnection;

import okhttp3.OkHttpClient;

public class AuthorizationHeaderConnection extends OkHttpDownloadConnection {
    public AuthorizationHeaderConnection(OkHttpClient okHttpClient, String url, String token) {
        super(okHttpClient, url);
        // add authorization header here.
        addHeader("AUTHORIZATION", token);
    }
    public static class Factory implements DownloadConnection.Factory {
        private OkHttpClient okHttpClient;
        private String token;

        public Factory(OkHttpClient okHttpClient, String token) {
            this.okHttpClient = okHttpClient;
            this.token = token;
        }

        @Override
        public DownloadConnection create(String url) {
            return new AuthorizationHeaderConnection(okHttpClient, url, token);
        }
    }
}
