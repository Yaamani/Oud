package com.example.oud.api;

public class StatusMessageResponse {
    private String status;
    private String message;

    public StatusMessageResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
