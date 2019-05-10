package com.example.appdasfinal.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectListFragment extends Fragment implements ServerRequestHandlerListener, Loader {

    JSONArray projects = new JSONArray();
    ProjectClickListener listener;

    RecyclerView projectsRecycler;
    ProjectRVAdapter projectRVAdapter;


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

        FloatingActionButton fab = view.findViewById(R.id.fab_add_project);
        fab.setOnClickListener(v -> alertDialogCreate());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        fetchProjects();
    }

    private void fetchProjects() {
        showProgress(true);
        ServerRequestHandler.getProjects(this);
    }

    @Override
    public void onGetProjectsSuccess(JSONArray jsonProjects) {
        projects = jsonProjects;
        updateProjectList();
        showProgress(false);
    }

    @Override
    public void onGetProjectsFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    private void updateProjectList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        projectsRecycler.setLayoutManager(linearLayoutManager);

        projectRVAdapter = new ProjectRVAdapter(projects);
        projectsRecycler.setAdapter(projectRVAdapter);

        projectRVAdapter.setOnItemClickListener(new ProjectRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                String id = null;
                try {
                    id = projects.getJSONObject(pos).getString("project_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onProjectClicked(id);
            }

            @Override
            public void onItemLongClick(int pos) {
                System.out.println("*************");
                System.out.println(pos);
                System.out.println(projects);
                System.out.println("*************");
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
                        alertDialogRename(projects.getJSONObject(pos).getString("name"), projects.getJSONObject(pos).getString("project_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        deleteProject(projects.getJSONObject(pos).getString("project_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });
        dialogBuilder.create().show();
    }

    private void alertDialogRename(String name, String id) {
        final EditText edittext = new EditText(getContext());
        edittext.setText(name);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.dialog_edit_title));

        dialogBuilder.setPositiveButton(getString(R.string.dialog_edit_save), (dialog, which) -> {
            String newName = edittext.getText().toString().trim();
            if (newName.equals("")) {
                ErrorNotifier.notifyEmptyField(getView());
            } else {
                renameProject(id, newName);
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.dialog_edit_discard), (dialog, which) -> {
        });

        dialogBuilder.setView(edittext);
        dialogBuilder.create().show();
    }

    private void alertDialogCreate() {
        final EditText edittext = new EditText(getContext());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.dialog_create_project_title));
        dialogBuilder.setView(edittext);

        dialogBuilder.setPositiveButton(getString(R.string.dialog_create_save), (dialog, which) -> {
            String name = edittext.getText().toString().trim();
            if (name.equals("")) {
                ErrorNotifier.notifyEmptyField(getView());
            } else {
                createProject(name);
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.dialog_create_discard), (dialog, which) -> {
        });

        dialogBuilder.create().show();
    }

    private void createProject(String name) {
        showProgress(true);
        ServerRequestHandler.createProject(name, this);
    }

    @Override
    public void onCreateProjectSuccess(String message, JSONObject jsonProject) {
        fetchProjects();
    }

    @Override
    public void onCreateProjectFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    private void deleteProject(String id) {
        showProgress(true);
        ServerRequestHandler.deleteProject(id, this);
    }

    @Override
    public void onDeleteProjectSuccess(String message) {
        fetchProjects();
    }

    @Override
    public void onDeleteProjectFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    private void renameProject(String id, String newName) {
        showProgress(true);
        ServerRequestHandler.updateProject(id, newName, this);
    }

    @Override
    public void onUpdateProjectSuccess(String message, JSONObject jsonProject) {
        fetchProjects();
    }

    @Override
    public void onUpdateProjectFailure(String message) {
        showProgress(false);
        ErrorNotifier.notifyServerError(getView(), message);
    }

    @Override
    public void onNoConnection() {
        showProgress(false);
        ErrorNotifier.notifyInternetConnection(getView());
    }

    @Override
    public View getContentView() {
        if (getView() != null) {
            return getView().findViewById(R.id.constraintLayout_projectList);
        }
        return null;
    }

    @Override
    public View getProgressBar() {
        if (getView() != null) {
            return getView().findViewById(R.id.progressBar_projectList);
        }
        return null;
    }

    public interface ProjectClickListener {

        void onProjectClicked(String id);
    }

}
