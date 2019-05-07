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

public class RequestRVAdapter extends RecyclerView.Adapter<RequestRVAdapter.ProjectViewHolder> {

    private JSONArray requests;

    private OnItemClickListener myListener;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        myListener = listener;
    }

    public RequestRVAdapter(JSONArray pRequests) {
        requests = pRequests;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View layoutInflater = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_request, viewGroup, false);
        return new ProjectViewHolder(layoutInflater, myListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder tournamentViewHolder, int i) {
        try {
            tournamentViewHolder.requestName.setText(requests.getJSONObject(i).getString("name"));
            tournamentViewHolder.requestMethod.setText(requests.getJSONObject(i).getString("method"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return requests.length();
    }


    /**
     * ViewHolder inner class
     */
    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        public TextView requestName;
        public TextView requestMethod;

        public ProjectViewHolder(View view, final OnItemClickListener listener) {
            super(view);

            requestName = view.findViewById(R.id.textView_request_name);
            requestMethod = view.findViewById(R.id.textView_request_method);

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
        }
    }

}

