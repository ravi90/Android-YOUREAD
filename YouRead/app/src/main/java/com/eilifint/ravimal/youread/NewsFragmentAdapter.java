package com.eilifint.ravimal.youread;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ravimal on 9/22/2016.
 */

public class NewsFragmentAdapter extends FragmentPagerAdapter {


    private Context context;

    //No of tabs
    final static int NO_OF_TABS = 3;

    /**
     * Constructor
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param fm      FragmentManager object for super class
     */
    public NewsFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        //if statement for return fragment objects according to position
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new CategoryFragment();
        } else {
            return new SearchFragment();
        }
    }

    @Override
    public int getCount() {
        return NO_OF_TABS;
    }
}
