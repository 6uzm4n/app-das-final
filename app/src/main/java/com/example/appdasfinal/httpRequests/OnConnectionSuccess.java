package com.example.appdasfinal.httpRequests;

import java.util.HashMap;

public interface OnConnectionSuccess {

    void onSuccess(int statusCode, String response, HashMap<String, String> headers);
}
