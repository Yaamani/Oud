package com.example.oud;

import com.example.tryingstuff.OudApiJsonGenerator;

import org.jetbrains.annotations.NotNull;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class TestUtils {

    public static MockWebServer getOudMockServer() {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.setDispatcher(getOudMockServerDispatcher());
        return mockWebServer;
    }

    private static Dispatcher getOudMockServerDispatcher() {
        Dispatcher dispatcher = new Dispatcher() {
            @NotNull
            @Override
            public MockResponse dispatch(@NotNull RecordedRequest recordedRequest) throws InterruptedException {
                String path = recordedRequest.getRequestUrl().encodedPath();
                switch (path) {
                    case "/me/player/recently-played":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonRecentlyPlayed());
                    case "/albums/album0":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(0));
                    case "/albums/album1":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(1));
                    case "/albums/album2":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(2));
                    case "/albums/album3":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(3));
                    case "/albums/album4":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(4));
                    case "/albums/album5":
                        return new MockResponse().setResponseCode(200).setBody(OudApiJsonGenerator.getJsonAlbum(5));
                }
                return new MockResponse().setResponseCode(404);
            }
        };

        return dispatcher;
    }

}
