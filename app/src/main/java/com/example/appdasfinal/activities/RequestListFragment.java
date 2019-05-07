package com.example.appdasfinal.activities;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appdasfinal.R;

import org.json.JSONArray;
import org.json.JSONException;


public class RequestListFragment extends Fragment {

    JSONArray requests;
    RequestClickListener listener;

    {
        try {
            requests = new JSONArray("[{id: '1', name: 'name1', method: 'GET'}, {id: '2', name: 'name2', method: 'POST'}]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    RecyclerView requestsRecycler;


    public RequestListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Attaches the listener
        listener = (RequestClickListener) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

        requestsRecycler = view.findViewById(R.id.recyclerview_requests);

        RequestRVAdapter requestRVAdapter = new RequestRVAdapter(requests);
        requestsRecycler.setAdapter(requestRVAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        requestsRecycler.setLayoutManager(linearLayoutManager);

        requestRVAdapter.setOnItemClickListener(new RequestRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String name = null;
                try {
                    name = requests.getJSONObject(pos).getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onRequestClicked(name);
            }
        });

        return view;
    }

    public interface RequestClickListener {
        void onRequestClicked(String id);
    }
}
