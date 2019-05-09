package com.example.appdasfinal.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.appdasfinal.R;

import java.util.HashMap;

public class RequestFragment extends Fragment {

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_request, container, false);

        LinearLayout headerList = view.findViewById(R.id.linearLayout_headers);
        Button button = view.findViewById(R.id.buttonAddHeader);
        button.setOnClickListener(v -> {
            CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.header, null);
            cardView.findViewById(R.id.imageView_remove_header).setOnClickListener(v2 -> {
                headerList.removeView(cardView);
            });
            headerList.addView(cardView);
        });
        return view;
    }

    private HashMap<String, String> getHeaders() {
        LinearLayout headerList = getView().findViewById(R.id.linearLayout_headers);
        HashMap<String, String> headers = new HashMap<>();
        for (int i = 0; i < headerList.getChildCount(); i++) {
            CardView headerCardView = (CardView) headerList.getChildAt(i);
            String key = ((TextView) headerCardView.findViewById(R.id.editText_header_key)).getText().toString();
            String value = ((TextView) headerCardView.findViewById(R.id.editText_header_value)).getText().toString();
            headers.put(key, value);
            System.out.println(key + ": " + value);
        }

        return headers;
    }

}
