package com.eilifint.ravimal.youread;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.github.rubensousa.floatingtoolbar.FloatingToolbar;

public class WebViewActivity extends AppCompatActivity implements FloatingToolbar.ItemClickListener,
        Toolbar.OnMenuItemClickListener, FloatingToolbar.MorphListener {

    private android.webkit.WebView webView;
    private String newsUrl;
    private FloatingToolbar mFloatingToolbar;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFloatingToolbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);

        mFloatingToolbar.setClickListener(this);
        mFloatingToolbar.attachFab(fab);
        mFloatingToolbar.addMorphListener(this);
        mFloatingToolbar.setClickListener(this);
        mFloatingToolbar.attachFab(fab);
        mFloatingToolbar.addMorphListener(this);


        //adding up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //getting the parsed valued through intent
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newsUrl = null;

            } else {
                newsUrl = extras.getString(this.getString(R.string.single_news));

            }
        } else {
            newsUrl = (String) savedInstanceState.getSerializable(this.getString(R.string.single_news));
            ((WebView) findViewById(R.id.web_view_news)).restoreState(savedInstanceState);
        }


        webView = (android.webkit.WebView) findViewById(R.id.web_view_news);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setLayerType(android.webkit.WebView.LAYER_TYPE_SOFTWARE, null);
        webView.clearSslPreferences();
        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mFloatingToolbar.
            }
        });
        webView.loadUrl(newsUrl);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFloatingToolbar.removeMorphListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_fb) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(newsUrl))
                    .build();

            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }
        if (item.getItemId() == R.id.action_email) {
            composeEmail(newsUrl);
        }



    }

    @Override
    public void onItemLongClick(MenuItem item) {

    }

    @Override
    public void onMorphEnd() {

    }

    @Override
    public void onMorphStart() {

    }

    @Override
    public void onUnmorphStart() {

    }

    @Override
    public void onUnmorphEnd() {

    }

    public void composeEmail( String attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_TEXT, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
