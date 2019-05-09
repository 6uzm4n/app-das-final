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

import java.util.Objects;


public class ResponseFragment extends Fragment {

    private Drawable arrowUp;
    private Drawable arrowDown;

    TextView textViewHeaders;
    TextView textViewBody;
    TextView textViewHeadersTitle;
    TextView textViewBodyTitle;


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
            textViewHeadersTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
        } else {
            textViewHeaders.setVisibility(View.VISIBLE);
            textViewHeadersTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
        }
    }

    private void toggleBodyVisibility(){
        if (textViewBody.getVisibility() == View.VISIBLE){
            textViewBody.setVisibility(View.GONE);
            textViewBodyTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowUp, null);
        } else {
            textViewBody.setVisibility(View.VISIBLE);
            textViewBodyTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDown, null);
        }
    }

}
