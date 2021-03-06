package com.eilifint.ravimal.youread;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Ravimal on 9/27/2016.
 */

public class CategoryNewsloader extends AsyncTaskLoader<List<News>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link CategoryNewsloader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public CategoryNewsloader(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of News.
        List<News> result = QueryUtils.fetchNewsData(mUrl, getContext());
        return result;
    }
}
