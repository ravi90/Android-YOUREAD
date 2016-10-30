package com.eilifint.ravimal.youread;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SingleCategoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * ProgressBar that is displayed when data loading
     */
    private ProgressBar mProgressBar;
    /**
     * list view refresh layout
     */
    SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * category news url
     */
    String url;
    /**
     * category type
     */
    String newsType;
    /**
     * mAdapter is a {@link NewsAdapter}  whose data source is a list of {@link News}s
     */
    private NewsAdapter mAdapter;
    /**
     * log tag
     */
    public static final String LOG_TAG = SingleCategoryActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        //adding up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getting the parsed valued through intent
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                url = null;
                newsType = null;
            } else {
                url = extras.getString(this.getString(R.string.put_url));
                newsType = extras.getString(this.getString(R.string.put_type));
                getSupportActionBar().setTitle(newsType + " " + this.getString(R.string.put_news));
            }
        } else {
            url = (String) savedInstanceState.getSerializable(this.getString(R.string.put_url));
        }

         /*Find the {@link ListView} object .
         There should be a {@link ListView} with the view ID called list, which is declared in the
         list.xml layout file.*/
        ListView listView = (ListView) findViewById(R.id.list);

        // Find the SwipeRefreshLayout in the list.xml layout for the refreshing list
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        // Find the TextView in the list.xml layout for the empty listView
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyStateTextView.setText("");
        // Find the ProgressBar in the list.xml layout for data loading
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView.setEmptyView(mEmptyStateTextView);

           /*Create an {@link ItemAdapter}, whose data source is a list of {@link Item}s. The
         adapter knows how to create list items for each item in the list.*/
        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);
        listView.setScrollingCacheEnabled(true);

        //list view on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);
                //Open new activity

                Intent webView = new Intent(SingleCategoryActivity.this, WebViewActivity.class);
                webView.putExtra(getString(R.string.single_news), currentNews.getWebUrl());
                startActivity(webView);

            }
        });

        //identify first element  in the listView to enable SwipeRefresh
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition;
                if (view == null || view.getChildCount() == 0) {
                    topRowVerticalPosition = 0;
                } else
                    topRowVerticalPosition = view.getChildAt(0).getTop();

                mSwipeRefreshLayout.setEnabled((topRowVerticalPosition >= 0));


            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            getLoaderManager().initLoader(2, null, this).forceLoad();
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(this.getString(R.string.no_internet));
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshContent();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * listView refresh by initialing {@link NewsLoader}
     */
    private void refreshContent() {

        getLoaderManager().initLoader(1, null, this).forceLoad();

    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        return new CategoryNewsloader(this, url);

    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        // Set empty state text to display "No news found."
        mEmptyStateTextView.setText(this.getString(R.string.snack_msg));
        mProgressBar.setVisibility(View.GONE);
        // Clear the adapter of previous news data
        mAdapter.clear();
        // If there is a valid list of {@link News}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
