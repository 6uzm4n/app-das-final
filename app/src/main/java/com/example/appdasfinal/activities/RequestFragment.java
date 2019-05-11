package com.example.appdasfinal.activities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.appdasfinal.R;
import com.example.appdasfinal.httpRequests.ServerRequestHandler;
import com.example.appdasfinal.httpRequests.ServerRequestHandlerListener;
import com.example.appdasfinal.utils.ErrorNotifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestFragment extends Fragment implements ServerRequestHandlerListener, Loader {

    private String id;
    private String name;
    private String url;
    private String method;
    private String body;
    private HashMap<String, String> headers;

    TextView urlTextView;
    Spinner methodSpinner;
    LinearLayout headerList;
    TextView bodyTextView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_request, container, false);

        urlTextView = view.findViewById(R.id.editText_url);
        methodSpinner = view.findViewById(R.id.spinner_method);
        headerList = view.findViewById(R.id.linearLayout_headers);
        bodyTextView = view.findViewById(R.id.editText_body);

        Button button = view.findViewById(R.id.buttonAddHeader);
        button.setOnClickListener(v -> addHeaderCardView());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (savedInstanceState != null) {
            showProgress(true);

            this.id = savedInstanceState.getString("id");
            this.name = savedInstanceState.getString("name");
            this.url = savedInstanceState.getString("url");
            this.method = savedInstanceState.getString("method");
            this.body = savedInstanceState.getString("body");
            this.headers = new HashMap<>();

            Bundle headersBundle = savedInstanceState.getBundle("headers");

            String bundleKeyKey = "header_%d_key";
            String bundleValueKey = "header_%d_value";

            for (int i = 0; i < headersBundle.getInt("size"); i++) {
                String headerKey = headersBundle.getString(String.format(bundleKeyKey, i));
                String headerValue = headersBundle.getString(String.format(bundleValueKey, i));
                this.headers.put(headerKey, headerValue);
            }

            setValues();

            showProgress(false);
        } else if (args != null) {
            this.id = args.getString("id");
            showProgress(true);
            ServerRequestHandler.getRequest(this.id, this);
        }
    }

    private CardView addHeaderCardView() {
        CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.cardview_header, null);
        cardView.findViewById(R.id.imageView_remove_header).setOnClickListener(v2 -> {
            headerList.removeView(cardView);
        });
        headerList.addView(cardView);
        return cardView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", this.id);
        outState.putString("name", this.name);
        outState.putString("url", this.getCurrentUrl());
        outState.putString("method", this.getCurrentMethod());
        outState.putString("body", this.getCurrentBody());

        HashMap<String, String> headers = getHeaders();
        Bundle headersBundle = new Bundle();
        headersBundle.putInt("size", headers.size());
        if (headers.size() > 0) {
            String bundleKeyKey = "header_%d_key";
            String bundleValueKey = "header_%d_value";
            int i = 0;

            for (Map.Entry<String, String> entry : headers.entrySet()) {
                headersBundle.putString(String.format(bundleKeyKey, i), entry.getKey());
                headersBundle.putString(String.format(bundleValueKey, i), entry.getValue());
                i++;
            }

        }
        outState.putBundle("headers", headersBundle);
    }

    private HashMap<String, String> getHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        for (int i = 0; i < headerList.getChildCount(); i++) {
            CardView headerCardView = (CardView) headerList.getChildAt(i);
            String key = ((TextView) headerCardView.findViewById(R.id.editText_header_key)).getText().toString();
            String value = ((TextView) headerCardView.findViewById(R.id.editText_header_value)).getText().toString();
            headers.put(key, value);
        }

        return headers;
    }

    @Override
    public void onGetRequestSuccess(JSONObject jsonRequest) {
        try {
            name = jsonRequest.getString("name");

            if (jsonRequest.isNull("url")) {
                url = jsonRequest.getString("");
            } else {
                url = jsonRequest.getString("url");
            }

            method = jsonRequest.getString("method");

            if (jsonRequest.isNull("body")) {
                body = "";
            } else {
                body = jsonRequest.getString("body");
            }

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
        showProgress(false);
    }

    @Override
    public void onGetRequestFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    private void setValues() {
        if (getView() == null) {
            return;
        }

        urlTextView.setText(this.url);

        for (int i = 0; i < methodSpinner.getCount(); i++) {
            if (methodSpinner.getItemAtPosition(i).toString().equals(this.method)) {
                methodSpinner.setSelection(i);
                break;
            }
        }

        bodyTextView.setText(this.body);

        headerList.removeAllViews();
        System.out.println("FOOOOOOKING KIIIIIILL MEEEEEE");
        System.out.println(this.headers.toString());
        System.out.println("FOOOOOOKING KIIIIIILL MEEEEEE");
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            CardView headerCardView = addHeaderCardView();
            TextView keyTextView = headerCardView.findViewById(R.id.editText_header_key);
            TextView valueTextView = headerCardView.findViewById(R.id.editText_header_value);

            keyTextView.setText(entry.getKey());
            valueTextView.setText(entry.getValue());
        }
    }

    public void saveRequest() {
        String id = this.id;
        String name = this.name;
        String url = getCurrentUrl();
        String body = getCurrentBody();
        String method = getCurrentMethod();
        HashMap<String, String> headers = getCurrentHeaders();
        ServerRequestHandler.updateRequest(
                id,
                name,
                url,
                body,
                method,
                headers,
                this
        );
        showProgress(true);
    }

    @Override
    public void onUpdateRequestSuccess(String message, JSONObject jsonRequest) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
        showProgress(false);
    }

    @Override
    public void onUpdateRequestFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    @Override
    public void onNoConnection() {
        showProgress(false);
        ErrorNotifier.notifyInternetConnection(getView());
    }

    @Override
    public View getContentView() {
        if (getView() != null) {
            return getView().findViewById(R.id.scrollView_requestFragment);
        }
        return null;
    }

    @Override
    public View getProgressBar() {
        if (getView() != null) {
            return getView().findViewById(R.id.progressBar_requestFragment);
        }
        return null;
    }

    public String getRequestId() {
        return this.id;
    }

    public String getRequestName() {
        return name;
    }

    public String getCurrentMethod() {
        // TODO: Cambiar si necesario
        return methodSpinner.getSelectedItem().toString();
    }

    public String getCurrentUrl() {
        // TODO: Cambiar si necesario
        return urlTextView.getText().toString();
    }

    public String getCurrentBody() {
        // TODO: Cambiar si necesario
        if (bodyTextView.getText().toString().equals("")) {
            return null;
        }
        return bodyTextView.getText().toString();
    }

    public HashMap<String, String> getCurrentHeaders() {
        // TODO: Cambiar si necesario
        return getHeaders();
    }
}
