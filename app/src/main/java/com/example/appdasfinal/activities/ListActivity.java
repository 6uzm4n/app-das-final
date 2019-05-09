package com.example.appdasfinal.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.appdasfinal.R;

public class ListActivity extends AppCompatActivity implements ProjectListFragment.ProjectClickListener, RequestListFragment.RequestClickListener {

    FrameLayout projectFrame;
    FrameLayout requestFrame;

    ProjectListFragment projectListFragment;
    RequestListFragment requestListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        projectFrame = findViewById(R.id.fragment_projects);
        requestFrame = findViewById(R.id.fragment_requests);

        projectListFragment = (ProjectListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_projects);
        requestListFragment = (RequestListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_requests);

        // Check orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (getSupportFragmentManager().findFragmentById(R.id.fragment_requests) != null) {
                projectFrame.setVisibility(View.GONE);
            }
        } else {
            projectFrame.setVisibility(View.VISIBLE);
        }

        projectListFragment = new ProjectListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_projects, projectListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                showLogoutDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProjectClicked(String id) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            projectFrame.setVisibility(View.GONE);
        }

        requestListFragment = new RequestListFragment();
        Bundle args = new Bundle();
        args.putString("project_id", id);
        requestListFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_requests, requestListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();

    }

    @Override
    public void onRequestClicked(String id) {
        Intent i = new Intent(ListActivity.this, RequestActivity.class);
        Bundle args = new Bundle();
        args.putString("id", id);
        i.putExtras(args);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if (requestListFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(requestListFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
            requestListFragment = null;
            projectFrame.setVisibility(View.VISIBLE);
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.dialog_exit_title));
        dialogBuilder.setMessage(getString(R.string.dialog_exit_message));
        CharSequence[] options = {getString(R.string.option_no), getString(R.string.option_yes)};

        dialogBuilder.setPositiveButton(R.string.option_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });

        dialogBuilder.setNegativeButton(R.string.option_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialogBuilder.create().show();
    }

    private void showLogoutDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getString(R.string.dialog_logout_title));
        dialogBuilder.setMessage(getString(R.string.dialog_logout_message));

        dialogBuilder.setPositiveButton(R.string.option_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();

            }
        });

        dialogBuilder.setNegativeButton(R.string.option_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialogBuilder.create().show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        sharedPreferences.edit().remove("session").apply();
        Intent i = new Intent(ListActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
