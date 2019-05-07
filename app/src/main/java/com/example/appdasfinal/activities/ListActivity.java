package com.example.appdasfinal.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    public void onProjectClicked(String id) {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            projectFrame.setVisibility(View.GONE);
        }

        requestListFragment = new RequestListFragment();
        Bundle args = new Bundle();
        args.putString("id", id);
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
            super.onBackPressed();
        }
    }
}
