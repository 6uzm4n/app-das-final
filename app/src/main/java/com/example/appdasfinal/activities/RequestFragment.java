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
    TextView bodyTitle;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_request, container, false);

        urlTextView = view.findViewById(R.id.editText_url);
        methodSpinner = view.findViewById(R.id.spinner_method);
        headerList = view.findViewById(R.id.linearLayout_headers);
        bodyTextView = view.findViewById(R.id.editText_body);
        bodyTitle = view.findViewById(R.id.textView_body_info);

        methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    bodyTextView.setVisibility(View.GONE);
                    bodyTitle.setVisibility(View.GONE);
                } else {
                    bodyTextView.setVisibility(View.VISIBLE);
                    bodyTitle.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = view.findViewById(R.id.buttonAddHeader);
        button.setOnClickListener(v -> addHeaderCardView());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
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

    private void setValues() {
        if (getView() == null) {
            return;
        }

        urlTextView.setText(this.url);
        headerList.removeAllViews();
        for (int i = 0; i < methodSpinner.getCount(); i++) {
            if (methodSpinner.getItemAtPosition(i).toString().equals(this.method)) {
                methodSpinner.setSelection(i);
                break;
            }
        }

        bodyTextView.setText(this.body);

        for (Map.Entry<String, String> entries : this.headers.entrySet()) {
            CardView headerCardView = addHeaderCardView();
            ((TextView) headerCardView.findViewById(R.id.editText_header_key)).setText(entries.getKey());
            ((TextView) headerCardView.findViewById(R.id.editText_header_value)).setText(entries.getValue());
        }
    }

    @Override
    public void onGetRequestSuccess(JSONObject jsonRequest) {
        try {
            name = jsonRequest.getString("name");

            if (jsonRequest.isNull("url")) {
                url = "";
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
        onGetRequestSuccess(jsonRequest);
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
        return bodyTextView.getText().toString();
    }

    public HashMap<String, String> getCurrentHeaders() {
        // TODO: Cambiar si necesario
        return getHeaders();
    }
}
