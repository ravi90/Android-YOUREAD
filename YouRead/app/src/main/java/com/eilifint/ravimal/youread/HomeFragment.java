package com.eilifint.ravimal.youread;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {


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
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * mAdapter is a {@link NewsAdapter}  whose data source is a list of {@link News}s
     */
    private NewsAdapter mAdapter;

    /**
     * Constant value for the News loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int News_LOADER_ID = 1;
    /**
     * log tag
     */

    public static final String LOG_TAG = HomeFragment.class.getName();


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //binding view to use the list.xml layout file
        View rootView = inflater.inflate(R.layout.list, container, false);

        /*Find the {@link ListView} object in the view hierarchy of the {@link Fragment}.
         There should be a {@link ListView} with the view ID called list, which is declared in the
         list.xml layout file.*/
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Find the SwipeRefreshLayout in the list.xml layout for the refreshing list
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);

        // Find the TextView in the list.xml layout for the empty listView
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        mEmptyStateTextView.setText("");

        // Find the ProgressBar in the list.xml layout for data loading
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        //set empty view
        listView.setEmptyView(mEmptyStateTextView);

           /*Create an {@link NewsAdapter}, whose data source is a list of {@link News}s. The
         adapter knows how to create list items for each item in the list.*/
        mAdapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);
        listView.setScrollingCacheEnabled(true);

        //list view on item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Open new activity
                News currentNews = mAdapter.getItem(position);
                Intent webView = new Intent(getContext(), WebViewActivity.class);
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
                //SwipeRefresh
                mSwipeRefreshLayout.setEnabled((topRowVerticalPosition >= 0));


            }
        });


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            //initialing the loader
            getLoaderManager().initLoader(News_LOADER_ID, null, this).forceLoad();
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
        return rootView;
    }

    /**
     * listView refresh by initialing {@link NewsLoader}
     */
    private void refreshContent() {

        getLoaderManager().initLoader(News_LOADER_ID, null, this).forceLoad();

    }


    @Override
    public android.support.v4.content.Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(getActivity(), buildUrl());
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<News>> loader, List<News> data) {

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
    public void onLoaderReset(android.support.v4.content.Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    /**
     * Build final url to retrieve News data
     */
    private String buildUrl() {
        Uri baseUri = Uri.parse(this.getString(R.string.base_url));
        Uri.Builder uriBuilder = baseUri.buildUpon();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        uriBuilder.appendQueryParameter(this.getString(R.string.page_size), pageSize);
        uriBuilder.appendQueryParameter(this.getString(R.string.show_fields), this.getString(R.string.fields));
        uriBuilder.appendQueryParameter(this.getString(R.string.show_references), this.getString(R.string.author_query));
        uriBuilder.appendQueryParameter(this.getString(R.string.order), orderBy);
        uriBuilder.appendQueryParameter(this.getString(R.string.api_key), this.getString(R.string.guarding_key));

        return uriBuilder.toString().replace(this.getString(R.string.replace), this.getString(R.string.comma));
    }

}
