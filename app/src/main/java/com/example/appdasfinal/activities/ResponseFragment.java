package com.example.appdasfinal.activities;


import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appdasfinal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class ResponseFragment extends Fragment {

    private Drawable arrowUp;
    private Drawable arrowDown;

    TextView textViewCode;
    TextView textViewHeadersTitle;
    TextView textViewHeaders;
    TextView textViewBodyTitle;
    TextView textViewBody;


    public ResponseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_response, container, false);

        arrowDown = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_drop_down);
        arrowUp = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_drop_up);

        textViewCode = view.findViewById(R.id.textView_code);

        textViewHeaders = view.findViewById(R.id.textView_headers);
        textViewHeaders.setHorizontallyScrolling(true);
        textViewHeaders.setMovementMethod(new ScrollingMovementMethod());
        textViewHeaders.setOnTouchListener((v, event) -> {
            textViewHeaders.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        textViewHeadersTitle = view.findViewById(R.id.textView_headers_title);
        textViewHeadersTitle.setOnClickListener(v -> toggleHeadersVisibility());

        textViewBody = view.findViewById(R.id.textView_body);
        textViewBody.setOnTouchListener((v, event) -> {
            textViewBody.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        textViewBodyTitle = view.findViewById(R.id.textView_body_title);
        textViewBodyTitle.setOnClickListener(v -> toggleBodyVisibility());

        return view;
    }

    private void toggleHeadersVisibility() {
        if (textViewHeaders.getVisibility() == View.VISIBLE) {
            textViewHeaders.setVisibility(View.GONE);
            textViewHeadersTitle.setCompoundDrawablesWithIntrinsicBounds(arrowUp, null, null, null);
        } else {
            textViewHeaders.setVisibility(View.VISIBLE);
            textViewHeadersTitle.setCompoundDrawablesWithIntrinsicBounds(arrowDown, null, null, null);
        }
    }

    private void toggleBodyVisibility() {
        if (textViewBody.getVisibility() == View.VISIBLE) {
            textViewBody.setVisibility(View.GONE);
            textViewBodyTitle.setCompoundDrawablesWithIntrinsicBounds(arrowUp, null, null, null);
        } else {
            textViewBody.setVisibility(View.VISIBLE);
            textViewBodyTitle.setCompoundDrawablesWithIntrinsicBounds(arrowDown, null, null, null);
        }
    }

    public void setResponse(int statusCode, HashMap<String, String> headers, String response) {
        textViewCode.setText(String.format(Locale.getDefault(), "%d", statusCode));
        StringBuilder headersText = new StringBuilder();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            headersText.append(entry.getKey());
            headersText.append(": ");
            headersText.append(entry.getValue());
            headersText.append("\n");
        }
        headersText.deleteCharAt(headersText.length() - 1);
        textViewHeaders.setText(headersText.toString());

        String responseText = response;
        textViewBody.setHorizontallyScrolling(false);
        String type = headers.get("Content-Type");
        if (type != null) {
            if (type.contains("application/json")) {
                try {
                    JSONObject responseJSON = new JSONObject(response);
                    responseText = responseJSON.toString(2);
                    textViewBody.setHorizontallyScrolling(true);
                    textViewBody.setMovementMethod(new ScrollingMovementMethod());
                } catch (JSONException e) {
                    try {
                        JSONArray responseJSON = new JSONArray(response);
                        responseText = responseJSON.toString(2);
                        textViewBody.setHorizontallyScrolling(true);
                        textViewBody.setMovementMethod(new ScrollingMovementMethod());
                    } catch (JSONException ignored) {
                    }
                }
            } else if (type.contains("text/html")) {
                Document doc = Jsoup.parse(response);
                responseText = doc.html();
                textViewBody.setHorizontallyScrolling(true);
                textViewBody.setMovementMethod(new ScrollingMovementMethod());
            }
        }

        textViewBody.setText(responseText);
    }
}
