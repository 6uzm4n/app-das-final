package com.example.appdasfinal.httpRequests;

import java.util.HashMap;

public interface OnConnectionFailure {

    void onFailure(int statusCode, String response, HashMap<String, String> headers);
}
