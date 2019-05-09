package com.example.appdasfinal.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.example.appdasfinal.R;


public class RequestActivity extends AppCompatActivity {

    RequestFragment requestFragment;
    ResponseFragment responseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        String id = null;
        Bundle args = getIntent().getExtras();
        if (args != null) {
            id = args.getString("id");
        }
        System.out.println("========================");
        System.out.println("ACTIVITY " + id);
        System.out.println("========================");

        requestFragment = new RequestFragment();
        requestFragment.setId(id);
        responseFragment = new ResponseFragment();

        // Setting the tabs
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
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
                    return requestFragment;
                case 1:
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

    public void foo() {
        responseFragment.foo();
    }
}