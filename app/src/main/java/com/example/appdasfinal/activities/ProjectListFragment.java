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
import com.example.appdasfinal.httpRequests.ServerRequestHandler;
import com.example.appdasfinal.utils.ErrorNotifier;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectListFragment extends Fragment implements ServerRequestHandler.ServerRequestHandlerListener {

    JSONArray projects;
    ProjectClickListener listener;

    {
        try {
            projects = new JSONArray("[]");
//            projects = new JSONArray("[{project_id: '1', name: 'name1'}, {project_id: '2', name: 'name2'}, {project_id: '3', name: 'name3'}]");
//            projects = new JSONArray("[{'_links':{},'name':'Pokemon PPP','project_id':'3bfc4181-ebc8-4f74-b9ec-d1bdaaf70904','requests':['89a1ece3-35ee-40dd-958a-962c0488879b','92802974-a38d-4969-9957-a0760777803c'],'user':'db02901d-f91b-4a21-9a1e-2b4249bdca73'},{'_links':{},'name':'Otra API','project_id':'33114a4c-bb66-437a-b859-f88754caff98','requests':[],'user':'db02901d-f91b-4a21-9a1e-2b4249bdca73'}]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCreate();
            }
        });

        fetchProjects();

        return view;
    }

    private void fetchProjects() {
        ServerRequestHandler.getProjects(this);
    }

    @Override
    public void onGetProjectsResponse(JSONArray jsonProjects) {
//        for (int i = 0; i < jsonProjects.length(); i++) {
//            try {
//                projects.put(jsonProjects.getJSONObject(i));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        projects = jsonProjects;
//        projectRVAdapter.notifyDataSetChanged();
        updateProjectList();
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
        dialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
            }
        });
        dialogBuilder.create().show();
    }

    private void alertDialogRename(String name, String id) {
        final EditText edittext = new EditText(getContext());
        edittext.setText(name);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.dialog_edit_title));

        dialogBuilder.setPositiveButton(getString(R.string.dialog_edit_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = edittext.getText().toString();
                if (newName.equals("")) {
                    ErrorNotifier.notifyEmptyField(getView());
                } else {
                    renameProject(id, newName);
                }
            }
        });

        dialogBuilder.setNegativeButton(getString(R.string.dialog_edit_discard), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialogBuilder.setView(edittext);
        dialogBuilder.create().show();
    }

    private void alertDialogCreate() {
        final EditText edittext = new EditText(getContext());
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle(getString(R.string.dialog_create_project_title));
        dialogBuilder.setView(edittext);

        dialogBuilder.setPositiveButton(getString(R.string.dialog_create_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edittext.getText().toString();
                if (name.equals("")) {
                    ErrorNotifier.notifyEmptyField(getView());
                } else {
                    createProject(edittext.getText().toString());
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

    private void createProject(String name) {
        ServerRequestHandler.createProject(name, this);
    }

    @Override
    public void onCreateProjectResponse(String message, JSONObject jsonProject) {
        if (jsonProject != null) {
            fetchProjects();
        } else {
            ErrorNotifier.notifyInternetConnection(getView());
        }
    }

    private void deleteProject(String id) {
        ServerRequestHandler.deleteProject(id, this);
    }

    @Override
    public void onDeleteProjectResponse(String message, boolean success) {
        if (success) {
            fetchProjects();
        } else {
            ErrorNotifier.notifyInternetConnection(getView());
        }
    }

    private void renameProject(String id, String newName) {
        ServerRequestHandler.updateProject(id, newName, this);
    }

    @Override
    public void onUpdateProjectResponse(String message, JSONObject jsonProject) {
        if (jsonProject != null) {
            fetchProjects();
        } else {
            ErrorNotifier.notifyInternetConnection(getView());
        }
    }

    public interface ProjectClickListener {
        void onProjectClicked(String id);
    }

}
