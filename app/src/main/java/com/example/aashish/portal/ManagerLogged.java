package com.example.aashish.portal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ManagerLogged extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;


    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_logged);
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int j = pref.getInt("key", 3);
        editor.putInt("key", 3);
        editor.apply();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager_logged, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.ChangePassword) {
            SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
            String e = pref.getString("passchange", "udate");
            String d = pref.getString("change", "update");
            Intent i = new Intent(getApplicationContext(), change.class);
            i.putExtra("check", d);
            i.putExtra("check2", e);
            startActivity(i);
        }
        if (id == R.id.Logout) {
            SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("key", 2);
            editor.apply();
            Intent i = new Intent(ManagerLogged.this, Login.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_manager_logged, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    AddEvent tabone = new AddEvent();
                    return tabone;

                case 1:
                    PendingEvents tabtwo = new PendingEvents();
                    return tabtwo;

                case 2:
                    EventsView tabthree = new EventsView();
                    return tabthree;
                default:
                    return null;

            }
        }

        @Override
        public int getCount() {

            return 3;
        }
    }
}