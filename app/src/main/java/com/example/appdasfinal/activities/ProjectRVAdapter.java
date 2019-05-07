package com.example.appdasfinal.activities;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.appdasfinal.R;

import org.json.JSONArray;
import org.json.JSONException;

public class ProjectRVAdapter extends RecyclerView.Adapter<ProjectRVAdapter.ProjectViewHolder> {

    private JSONArray projects;

    private OnItemClickListener myListener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
        void onItemLongClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }

    public ProjectRVAdapter(JSONArray pProjects) {
        projects = pProjects;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutInflater = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_project, viewGroup, false);
        return new ProjectViewHolder(layoutInflater, myListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder tournamentViewHolder, int i) {
        try {
            tournamentViewHolder.projectName.setText(projects.getJSONObject(i).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return projects.length();
    }


    /**
     * ViewHolder inner class
     */
    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        public TextView projectName;

        public ProjectViewHolder(View view, final OnItemClickListener listener) {
            super(view);

            projectName = view.findViewById(R.id.textView_project_name);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(position);
                            return true;
                        }
                    }
                    return false;
                }

            });
        }
    }

}

