package com.eilifint.ravimal.youread;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {


    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;
    /**
     * editTextview to type search item
     */
    private EditText mSearchEditText;
    /**
     * ProgressBar that is displayed when data loading
     */
    private ProgressBar mProgressBar;
    /**
     * search button
     */
    private Button mSearch;
    /**
     * search value for query
     */
    String searchValue;
    /**
     * CoordinatorLayout layout which support snackBar
     */
    CoordinatorLayout coordinatorLayout;
    /**
     * mAdapter is a {@link NewsAdapter}  whose data source is a list of {@link News}s
     */
    private NewsAdapter mAdapter;
    /**
     * log tag
     */

    /**
     * Constant value for the News loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int News_LOADER_ID = 1;

    public static final String LOG_TAG = HomeFragment.class.getName();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //binding view to use the list.xml layout file
        View rootView = inflater.inflate(R.layout.search_list, container, false);

        /*Find the {@link ListView} object in the view hierarchy of the {@link Fragment}.
         There should be a {@link ListView} with the view ID called list, which is declared in the
         search_list.xml layout file.*/
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Find the TextView in the search_list.xml layout for the empty listView
        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);

        // Find the ProgressBar in the search_list.xml layout for data loading
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        // Find the Button in the search_list.xml layout for searching
        mSearch = (Button) rootView.findViewById(R.id.search_button);
        // Find the EditText in the search_list.xml layout
        mSearchEditText = (EditText) rootView.findViewById(R.id.edit_text_news);

        // Find the CoordinatorLayout in the search_list.xml layout
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);
        //set emptyView
        listView.setEmptyView(mEmptyStateTextView);
        mEmptyStateTextView.setText("");
           /*Create an {@link ItemAdapter}, whose data source is a list of {@link Item}s. The
         adapter knows how to create list items for each item in the list.*/
        mAdapter = new NewsAdapter(getActivity(), new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        listView.setAdapter(mAdapter);
        listView.setScrollingCacheEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);
                //Open new activity
                Intent webView = new Intent(getContext(), WebViewActivity.class);
                webView.putExtra(getString(R.string.single_news), currentNews.getWebUrl());
                startActivity(webView);

            }
        });

        //initialing the loader
        getLoaderManager().initLoader(News_LOADER_ID, null, SearchFragment.this).forceLoad();


        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //search button click event
        mSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                searchValue = mSearchEditText.getText().toString().trim();
                if (!searchValue.equals("")) {
                    // If there is a network connection, fetch data
                    if (networkInfo != null && networkInfo.isConnected()) {

                        //restarting the loader
                        getLoaderManager().restartLoader(News_LOADER_ID, null, SearchFragment.this);
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                        mEmptyStateTextView.setText(getString(R.string.no_internet));
                    }
                } else {
                    // show a snack bar message after loading book information
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getString(R.string.snack_msg), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                // hide soft keyboard
                try {
                    InputMethodManager inputMethod = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethod.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    Log.e(LOG_TAG, getActivity().getString(R.string.exception), e);
                }
            }
        });

        //return view
        return rootView;
    }

    /**
     * Build final url to retrieve News data
     *
     * @param type is news type
     */
    private String buildUrl(String type) {
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
        uriBuilder.appendQueryParameter(this.getString(R.string.query), type);
        uriBuilder.appendQueryParameter(this.getString(R.string.page_size), pageSize);
        uriBuilder.appendQueryParameter(this.getString(R.string.show_fields), this.getString(R.string.fields));
        uriBuilder.appendQueryParameter(this.getString(R.string.show_references), this.getString(R.string.author_query));
        uriBuilder.appendQueryParameter(this.getString(R.string.order), orderBy);
        uriBuilder.appendQueryParameter(this.getString(R.string.api_key), this.getString(R.string.guarding_key));

        return uriBuilder.toString().replace(this.getString(R.string.replace), this.getString(R.string.comma));

    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(getActivity(), buildUrl(searchValue));

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

        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
