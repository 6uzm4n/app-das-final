package com.example.appdasfinal.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appdasfinal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment {


    public RequestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("ON CREATE" + this.getClass().getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("ON CREATE VIEW" + this.getClass().getName());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request, container, false);
    }

}
