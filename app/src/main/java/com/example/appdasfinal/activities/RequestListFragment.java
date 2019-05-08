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
import com.example.appdasfinal.httpRequests.HTTPRequestSender;
import com.example.appdasfinal.httpRequests.ServerRequestHandler;
import com.example.appdasfinal.utils.ErrorNotifier;
import com.example.appdasfinal.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class RequestListFragment extends Fragment implements ServerRequestHandler.ServerRequestHandlerListener {

    String id;

    JSONArray requests;
    RequestClickListener listener;

    {
        try {
            requests = new JSONArray("[]");
//            requests = new JSONArray("[{id: '1', name: 'name1', method: 'GET'}, {id: '2', name: 'name2', method: 'POST'}]");
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

        // Get args
        if (getArguments() != null) {
            id = getArguments().getString("project_id");
        }

        requestsRecycler = view.findViewById(R.id.recyclerview_requests);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_request);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCreate();
            }
        });

        fetchRequests();

        return view;
    }

    private void fetchRequests() {
        ServerRequestHandler.getRequests(id, this);
    }

    @Override
    public void onGetRequestsResponse(JSONArray jsonRequests) {
        requests = jsonRequests;
        updateRequestsList();
    }

    private void updateRequestsList() {
        requestRVAdapter = new RequestRVAdapter(requests);
        requestsRecycler.setAdapter(requestRVAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        requestsRecycler.setLayoutManager(linearLayoutManager);

        requestRVAdapter.setOnItemClickListener(new RequestRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String name = null;
                try {
                    name = requests.getJSONObject(pos).getString("request_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onRequestClicked(name);
            }

            @Override
            public void onItemLongClick(int pos) {
                alertDialogAction(pos);
            }
        });
    }

    private void alertDialogAction(int pos) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        CharSequence[] options = {getString(R.string.dialog_edit), getString(R.string.dialog_delete)};
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        try {
                            System.out.println("RRRRRRRRRRR");
                            System.out.println(requests.getJSONObject(pos));
                            System.out.println("RRRRRRRRRRR");
                            alertDialogRename(requests.getJSONObject(pos));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            deleteRequest(requests.getJSONObject(pos).getString("request_id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        dialogBuilder.create().show();
    }

    private void alertDialogRename(JSONObject request) {
        final EditText edittext = new EditText(getContext());
        try {
            edittext.setText(request.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                    renameRequest(request, newName);
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
                    createRequest(edittext.getText().toString());
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

    private void createRequest(String name) {
        ServerRequestHandler.createRequest(id, name, this);
    }

    @Override
    public void onCreateRequestResponse(String message, JSONObject jsonRequest) {
        if (jsonRequest != null) {
            fetchRequests();
        } else {
            ErrorNotifier.notifyInternetConnection(getView());
        }
    }

    private void deleteRequest(String id) {
        ServerRequestHandler.deleteRequest(id, this);
    }

    @Override
    public void onDeleteRequestResponse(String message, boolean success) {
        if (success) {
            fetchRequests();
        } else {
            ErrorNotifier.notifyInternetConnection(getView());
        }
    }

    private void renameRequest(JSONObject request, String newName) {
        String requestId = null;
        String method = null;
        String body = null;
        String url = null;
        HashMap<String, String> headers = new HashMap<>();
        try {
            requestId = request.getString("request_id");
            url = request.getString("url");
            body = request.getString("body");
            method = request.getString("method");
            JSONArray headersJSON = request.getJSONArray("headers");
            for (int i = 0; i < headersJSON.length(); i++) {
                headers.put(headersJSON.getJSONObject(i).getString("key"), headersJSON.getJSONObject(i).getString("value"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Utils.requireNotNull(requestId, newName, url, body, method, headers)) {
            ServerRequestHandler.updateRequest(requestId, newName, url, body, method, headers, this);
        }
    }

    @Override
    public void onUpdateRequestResponse(String message, JSONObject jsonRequest) {
        if (jsonRequest != null) {
            fetchRequests();
        } else {
            ErrorNotifier.notifyInternetConnection(getView());
        }
    }

    // TODO: Rename listener

    public interface RequestClickListener {
        void onRequestClicked(String id);
    }
}
