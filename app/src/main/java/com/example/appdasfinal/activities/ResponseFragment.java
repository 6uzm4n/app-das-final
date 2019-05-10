package com.example.appdasfinal.activities;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appdasfinal.R;

import java.util.HashMap;
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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_response, container, false);

        arrowDown = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_drop_down);
        arrowUp = ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.ic_arrow_drop_up);

        textViewCode = view.findViewById(R.id.textView_code);

        textViewHeaders = view.findViewById(R.id.textView_headers);
        textViewHeadersTitle = view.findViewById(R.id.textView_headers_title);
        textViewHeadersTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleHeadersVisibility();
            }
        });

        textViewBody = view.findViewById(R.id.textView_body);
        textViewBodyTitle = view.findViewById(R.id.textView_body_title);
        textViewBodyTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBodyVisibility();
            }
        });

        return view;
    }

    private void toggleHeadersVisibility(){
        if (textViewHeaders.getVisibility() == View.VISIBLE){
            textViewHeaders.setVisibility(View.GONE);
            textViewHeadersTitle.setCompoundDrawablesWithIntrinsicBounds(arrowUp, null, null, null);
        } else {
            textViewHeaders.setVisibility(View.VISIBLE);
            textViewHeadersTitle.setCompoundDrawablesWithIntrinsicBounds(arrowDown, null, null, null);
        }
    }

    private void toggleBodyVisibility(){
        if (textViewBody.getVisibility() == View.VISIBLE){
            textViewBody.setVisibility(View.GONE);
            textViewBodyTitle.setCompoundDrawablesWithIntrinsicBounds(arrowUp, null, null, null);
        } else {
            textViewBody.setVisibility(View.VISIBLE);
            textViewBodyTitle.setCompoundDrawablesWithIntrinsicBounds(arrowDown, null, null, null);
        }
    }

    public void setResponse(int statusCode, HashMap<String, String> headers, String response) {
        textViewCode.setText(Integer.toString(statusCode));
        textViewHeaders.setText(headers.toString());
        textViewBody.setText(response);
    }
}
