package com.example.appdasfinal.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.appdasfinal.R;
import com.example.appdasfinal.utils.ErrorNotifier;

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
    RequestRVAdapter requestRVAdapter;


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

        requestRVAdapter = new RequestRVAdapter(requests);
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

            @Override
            public void onItemLongClick(int pos) {
                alertDialogAction();
            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add_request);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCreate();
            }
        });

        return view;
    }

    private void alertDialogAction() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        CharSequence[] options = {getString(R.string.dialog_edit), getString(R.string.dialog_delete)};
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        try {
                            alertDialogRename(requests.getJSONObject(which).getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            deleteRequest(requests.getJSONObject(which).getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        dialogBuilder.create().show();
    }

    private void alertDialogRename(String id) {
        final EditText edittext = new EditText(getContext());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.dialog_edit_title));
        dialogBuilder.setView(edittext);

        dialogBuilder.setPositiveButton(getString(R.string.dialog_edit_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = edittext.getText().toString();
                if (newName.equals("")) {
                    ErrorNotifier.notifyEmptyField(getView());
                } else {
                    renameRequest(id, newName);
                }
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.dialog_edit_discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialogBuilder.create().show();
    }

    private void alertDialogCreate() {
        final EditText edittext = new EditText(getContext());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.dialog_create_request_title));
        dialogBuilder.setView(edittext);

        dialogBuilder.setPositiveButton(getString(R.string.dialog_create_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edittext.getText().toString();
                if (name.equals("")) {
                    ErrorNotifier.notifyEmptyField(getView());
                } else {
                    addRequest(edittext.getText().toString());
                }
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.dialog_create_discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialogBuilder.create().show();
    }

    private void addRequest(String name) {
        // TODO: Add
    }

    private void deleteRequest(String id) {
        // TODO: Delete
        requestRVAdapter.notifyDataSetChanged();
    }

    private void renameRequest(String id, String newName) {
        // TODO: Rename
    }

    public interface RequestClickListener {
        void onRequestClicked(String id);
    }
}
