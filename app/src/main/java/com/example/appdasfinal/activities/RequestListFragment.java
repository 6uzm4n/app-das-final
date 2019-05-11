package com.example.appdasfinal.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.appdasfinal.R;
import com.example.appdasfinal.httpRequests.ServerRequestHandler;
import com.example.appdasfinal.httpRequests.ServerRequestHandlerListener;
import com.example.appdasfinal.utils.ErrorNotifier;
import com.example.appdasfinal.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class RequestListFragment extends Fragment implements ServerRequestHandlerListener, Loader {

    String id;

    JSONArray requests = new JSONArray();
    RequestClickListener listener;

    SwipeRefreshLayout refreshLayout;
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

        refreshLayout = view.findViewById(R.id.refreshLayout_requests);
        refreshLayout.setOnRefreshListener(() -> ServerRequestHandler.getRequests(id, this));
        requestsRecycler = view.findViewById(R.id.recyclerview_requests);

        FloatingActionButton fab = view.findViewById(R.id.fab_add_request);
        fab.setOnClickListener(v -> alertDialogCreate());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchRequests();
    }

    private void fetchRequests() {
        showProgress(true);
        ServerRequestHandler.getRequests(id, this);
    }

    @Override
    public void onGetRequestsSuccess(JSONArray jsonRequests) {
        requests = jsonRequests;
        updateRequestsList();
        showProgress(false);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onGetRequestsFailure(String message) {
        showProgress(false);
        refreshLayout.setRefreshing(false);
        ErrorNotifier.notifyServerError(getView(), message);
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
        dialogBuilder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    try {
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

        dialogBuilder.setPositiveButton(getString(R.string.dialog_edit_save), (dialog, which) -> {
            String newName = edittext.getText().toString().trim();
            if (newName.equals("")) {
                ErrorNotifier.notifyEmptyField(getView());
            } else {
                renameRequest(request, newName);
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.dialog_edit_discard), (dialog, which) -> {
        });

        dialogBuilder.create().show();
    }

    private void alertDialogCreate() {
        final EditText edittext = new EditText(getContext());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.dialog_create_request_title));
        dialogBuilder.setView(edittext);

        dialogBuilder.setPositiveButton(getString(R.string.dialog_create_save), (dialog, which) -> {
            String name = edittext.getText().toString().trim();
            if (name.equals("")) {
                ErrorNotifier.notifyEmptyField(getView());
            } else {
                createRequest(name);
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.dialog_create_discard), (dialog, which) -> {
        });

        dialogBuilder.create().show();
    }

    private void createRequest(String name) {
        showProgress(true);
        ServerRequestHandler.createRequest(id, name, this);
    }

    @Override
    public void onCreateRequestSuccess(String message, JSONObject jsonRequest) {
        fetchRequests();
    }

    @Override
    public void onCreateRequestFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    private void deleteRequest(String id) {
        showProgress(true);
        ServerRequestHandler.deleteRequest(id, this);
    }

    @Override
    public void onDeleteRequestSuccess(String message) {
        fetchRequests();
    }

    @Override
    public void onDeleteRequestFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    private void renameRequest(JSONObject request, String newName) {
        String requestId = null;
        try {
            requestId = request.getString("request_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Utils.requireNotNull(requestId, newName)) {
            showProgress(true);
            ServerRequestHandler.updateRequest(requestId, newName, null, null, null, null, this);
        }
    }

    @Override
    public void onUpdateRequestSuccess(String message, JSONObject jsonRequest) {
        fetchRequests();
    }

    @Override
    public void onUpdateRequestFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    @Override
    public void onNoConnection() {
        showProgress(false);
        refreshLayout.setRefreshing(false);
        ErrorNotifier.notifyInternetConnection(getView());
    }

    @Override
    public View getContentView() {
        if (getView() != null) {
            return getView().findViewById(R.id.constraintLayout_requestList);
        }
        return null;
    }

    @Override
    public View getProgressBar() {
        if (getView() != null) {
            return getView().findViewById(R.id.progressBar_requestList);
        }
        return null;
    }

    public interface RequestClickListener {

        void onRequestClicked(String id);
    }
}
