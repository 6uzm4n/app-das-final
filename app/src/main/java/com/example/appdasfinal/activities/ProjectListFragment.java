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

public class ProjectListFragment extends Fragment {

    JSONArray projects;
    ProjectClickListener listener;

    {
        try {
            projects = new JSONArray("[{id: '1', name: 'name1'}, {id: '2', name: 'name2'}, {id: '3', name: 'name3'}]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    RecyclerView projectsRecycler;


    public ProjectListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Attaches the listener
        listener = (ProjectClickListener) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project_list, container, false);

        projectsRecycler = view.findViewById(R.id.recyclerview_projects);

        ProjectRVAdapter projectRVAdapter = new ProjectRVAdapter(projects);
        projectsRecycler.setAdapter(projectRVAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        projectsRecycler.setLayoutManager(linearLayoutManager);

        projectRVAdapter.setOnItemClickListener(new ProjectRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String id = null;
                try {
                    id = projects.getJSONObject(pos).getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onProjectClicked(id);
            }
        });

        return view;
    }

    public interface ProjectClickListener {
        void onProjectClicked(String id);
    }
}
