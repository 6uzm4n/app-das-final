package com.example.appdasfinal.activities;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.appdasfinal.R;
import com.example.appdasfinal.httpRequests.*;
import com.example.appdasfinal.utils.ErrorNotifier;

import java.util.HashMap;


public class RequestActivity extends AppCompatActivity implements OnConnectionSuccess, OnConnectionFailure, ServerRequestHandlerListener {

    private ViewPager viewPager;

    RequestFragment requestFragment;
    ResponseFragment responseFragment;

    FloatingActionButton fabSend;
    FloatingActionButton fabSave;

    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Bundle args = getIntent().getExtras();
        if (args != null) {
            this.id = args.getString("id");
        }

        fabSend = findViewById(R.id.floatingActionButton_send);
        fabSend.setOnClickListener(v -> sendRequest());

        fabSave = findViewById(R.id.floatingActionButton_save);
        fabSave.setOnClickListener(v -> requestFragment.saveRequest());

        // Setting the tabs
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                showButtons(i == 0);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @SuppressLint("RestrictedApi")
    private void showButtons(boolean show) {
        if (!show) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fabSave.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            fabSave.setLayoutParams(p);
            fabSave.setVisibility(View.GONE);

            p = (CoordinatorLayout.LayoutParams) fabSend.getLayoutParams();
            p.setAnchorId(View.NO_ID);
            fabSend.setLayoutParams(p);
            fabSend.setVisibility(View.GONE);
        } else {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fabSave.getLayoutParams();
            p.setAnchorId(R.id.view_pager);
            fabSave.setLayoutParams(p);
            fabSave.setVisibility(View.VISIBLE);

            p = (CoordinatorLayout.LayoutParams) fabSend.getLayoutParams();
            p.setAnchorId(R.id.view_pager);
            fabSend.setLayoutParams(p);
            fabSend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }

    }

    private void sendRequest() {
        HTTPRequestSender sender = HTTPRequestSender.getInstance();
        String method = requestFragment.getCurrentMethod();
        String url = requestFragment.getCurrentUrl();
        String body = requestFragment.getCurrentBody();
        HashMap<String, String> headers = requestFragment.getCurrentHeaders();
        HttpRequest.Builder builder = sender.customRequest(method, url, body, headers);
        builder.run(this, this);
        requestFragment.showProgress(true);
    }

    @Override
    public void onSuccess(int statusCode, String response, HashMap<String, String> headers) {
        responseFragment.setResponse(statusCode, headers, response);
        requestFragment.showProgress(false);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onFailure(int statusCode, String response, HashMap<String, String> headers) {
        responseFragment.setResponse(statusCode, headers, response);
        requestFragment.showProgress(false);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onNoConnection() {
        requestFragment.showProgress(false);
        ErrorNotifier.notifyInternetConnection(getWindow().getDecorView().getRootView());
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    requestFragment = new RequestFragment();
                    Bundle args = new Bundle();
                    args.putString("id", id);
                    requestFragment.setArguments(args);
                    return requestFragment;
                case 1:
                    responseFragment = new ResponseFragment();
                    return responseFragment;
            }
            return null;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.tab_request);
                case 1:
                    return getString(R.string.tab_response);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }

}