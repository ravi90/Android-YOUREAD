package com.eilifint.ravimal.youread;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int tabCount;

        //set title to action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(this.getString(R.string.title_home));


        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        NewsFragmentAdapter adapter = new NewsFragmentAdapter(getSupportFragmentManager(), MainActivity.this);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabCount = tabLayout.getTabCount();

        //setting tab icons
        for (int i = 0; i < tabCount; i++) {
            if (i == 0)
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_home);
            else if (i == 1)
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_category);
            else
                tabLayout.getTabAt(i).setIcon(R.drawable.ic_search);
        }

        //Change action bar title for each tab swipe
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                if (position == 0)
                    getSupportActionBar().setTitle(getString(R.string.title_home));
                else if (position == 1)
                    getSupportActionBar().setTitle(getString(R.string.title_cat));
                else
                    getSupportActionBar().setTitle(getString(R.string.title_search));
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int pos) {
                // TODO Auto-generated method stub

            }
        });
    }
}
