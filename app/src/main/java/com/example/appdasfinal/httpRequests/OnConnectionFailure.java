package com.example.appdasfinal.httpRequests;

public interface OnConnectionFailure {

    void onFailure(int statusCode, String response);
}
