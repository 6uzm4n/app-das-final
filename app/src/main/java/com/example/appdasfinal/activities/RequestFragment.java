package com.example.appdasfinal.activities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appdasfinal.R;
import com.example.appdasfinal.httpRequests.ServerRequestHandler;
import com.example.appdasfinal.httpRequests.ServerRequestHandlerListener;
import com.example.appdasfinal.utils.ErrorNotifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestFragment extends Fragment implements ServerRequestHandlerListener {

    String id;

    String url;
    String method;
    String body;
    HashMap<String, String> headers;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_request, container, false);



        Button button = view.findViewById(R.id.buttonAddHeader);
        button.setOnClickListener(v -> addHeaderCardView());
        return view;
    }

    private CardView addHeaderCardView() {
        LinearLayout headerList = getView().findViewById(R.id.linearLayout_headers);
        CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.header, null);
        cardView.findViewById(R.id.imageView_remove_header).setOnClickListener(v2 -> {
            headerList.removeView(cardView);
        });
        headerList.addView(cardView);
        return cardView;
    }

    private HashMap<String, String> getHeaders() {
        LinearLayout headerList = getView().findViewById(R.id.linearLayout_headers);
        HashMap<String, String> headers = new HashMap<>();
        for (int i = 0; i < headerList.getChildCount(); i++) {
            CardView headerCardView = (CardView) headerList.getChildAt(i);
            String key = ((TextView) headerCardView.findViewById(R.id.editText_header_key)).getText().toString();
            String value = ((TextView) headerCardView.findViewById(R.id.editText_header_value)).getText().toString();
            headers.put(key, value);
        }

        return headers;
    }

    public void setId(String id) {
        System.out.println("??????????????????");
        System.out.println("ID SET " + id);
        System.out.println("??????????????????");
        this.id = id;
        if (getView() != null) {
            ServerRequestHandler.getRequest(id, this);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        System.out.println("VIEW CREATED");
        super.onViewCreated(view, savedInstanceState);
        if (id != null){
            ServerRequestHandler.getRequest(id, this);
        }
    }

    public void setValues() {
        TextView urlTextView = getView().findViewById(R.id.editText_url);
        urlTextView.setText(this.url);
        Spinner spinnerMethod = getView().findViewById(R.id.spinner_method);
        for (int i=0;i<spinnerMethod.getCount();i++){
            if (spinnerMethod.getItemAtPosition(i).toString().equals(method)){
                spinnerMethod.setSelection(i);
                break;
            }
        }
        urlTextView.setText(this.url);
        TextView bodyTextView = getView().findViewById(R.id.editText_body);
        bodyTextView.setText(this.body);
        for (Map.Entry<String, String> entries : this.headers.entrySet()) {
            CardView headerCardView = addHeaderCardView();
            ((TextView) headerCardView.findViewById(R.id.editText_header_key)).setText(entries.getKey());
            ((TextView) headerCardView.findViewById(R.id.editText_header_value)).setText(entries.getValue());
        }
    }

    @Override
    public void onGetRequestSuccess(JSONObject jsonRequest) {
        System.out.println("*******************");
        System.out.println(jsonRequest);
        System.out.println("*******************");
        try {
            url = jsonRequest.getString("url");
            method = jsonRequest.getString("method");
            body = jsonRequest.getString("body");
            JSONArray headersJson = jsonRequest.getJSONArray("headers");
            headers = new HashMap<>();
            for (int i = 0; i < headersJson.length(); i++) {
                JSONObject header = headersJson.getJSONObject(i);
                headers.put(header.getString("key"), header.getString("value"));
            }
            setValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetRequestFailure(String message) {
        ErrorNotifier.notifyServerError(getView(), message);
    }
}
