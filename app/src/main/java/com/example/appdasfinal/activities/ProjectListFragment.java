package com.example.appdasfinal.activities;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import org.json.JSONObject;

import java.util.Objects;

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

        projectRVAdapter = new ProjectRVAdapter(projects);
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

            @Override
            public void onItemLongClick(int pos) {
                alertDialogAction();
            }


        });

        FloatingActionButton fab = view.findViewById(R.id.fab_add_project);
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
                            alertDialogRename(projects.getJSONObject(which).getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            deleteProject(projects.getJSONObject(which).getString("id"));
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
                    addProject(edittext.getText().toString());
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

    private void addProject(String name) {
        // TODO: Add

        // Test
        try {
            projects.put(new JSONObject("{id: 'AA', name: '" + name + "'}"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deleteProject(String id) {
        // TODO: Delete
        projectRVAdapter.notifyDataSetChanged();
    }

    private void renameProject(String id, String newName) {
        // TODO: Rename
    }

    public interface ProjectClickListener {
        void onProjectClicked(String id);
    }


}
