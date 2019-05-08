package com.example.appdasfinal.activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.appdasfinal.R;

public class RequestFragment extends Fragment {

    public RequestFragment() {
        // Required empty public constructor
    }

    private LinearLayout list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_request, container, false);

        list = view.findViewById(R.id.linearLayout_headers);
        Button button = view.findViewById(R.id.buttonAddHeader);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = new TextView(getContext());
                textView.setText("HOLA");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        list.removeView(textView);
                    }
                });
                list.addView(textView);
            }
        });


        return view;
    }

}
