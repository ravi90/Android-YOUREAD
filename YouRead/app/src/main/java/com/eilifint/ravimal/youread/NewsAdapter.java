package com.eilifint.ravimal.youread;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Ravimal on 9/26/2016.
 * {@link NewsAdapter} is an {@link ArrayAdapter} that can provide the layout for each news
 * based on a data source, which is a list of {@link News} objects.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Create a new {@link NewsAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param objects is the list of {@link Category}s to be displayed.
     */
    public NewsAdapter(Context context, List<News> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);

        }

        // Get the {@link News} object located at this position in the list
        News currentItem = getItem(position);

        // Create a new {@link Holder} object
        Holder newsHolder = new Holder();

        // Find the TextView in the news_item.xml layout for the webTitle
        newsHolder.webTitle = (TextView) listItemView.findViewById(R.id.text_title);
        //set  text on the webTitle TextView
        newsHolder.webTitle.setText(currentItem.getWebTitle());

        // Find the TextView in the news_item.xml layout for the author
        newsHolder.author = (TextView) listItemView.findViewById(R.id.text_author);
        //set  text on the author TextView
        newsHolder.author.setText(getContext().getString(R.string.author) + currentItem.getAuthor());

        // Find the TextView in the news_item.xml layout for the webPublicationDate
        newsHolder.webPublicationDate = (TextView) listItemView.findViewById(R.id.text_date);
        //set  text on the webPublicationDate TextView
        newsHolder.webPublicationDate.setText(getContext().getString(R.string.published_date) + " " + currentItem.getWebPublicationDate());

        // Find the TextView in the news_item.xml layout for the trailTex
        newsHolder.trailTex = (TextView) listItemView.findViewById(R.id.text_trail);
        //set  text on the trailTex TextView
        newsHolder.trailTex.setText(currentItem.getTrailTex());

        // if currentItem {@link News} thumbnail value is not equal to "Not available"
        //then {@link AsyncTask} to load thumbnails
        if (!currentItem.getThumbnailUrl().equals(getContext().getString(R.string.not_available))) {
            new DownloadImageTask((ImageView) listItemView.findViewById(R.id.image_news))
                    .execute(currentItem.getThumbnailUrl());
        }

        // Return the whole list item layout so that it can be shown the ListView.
        return listItemView;
    }

    /**
     * {@link Holder} is to hold textViews
     */
    private static class Holder {

        // textViews declaration
        private TextView webTitle;
        private TextView trailTex;
        private TextView author;
        private TextView webPublicationDate;


    }


    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the book thumbnails in the response.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        // Declaring of ImageView
        private ImageView newsThumbnail;

        /**
         * Create a new {@link DownloadImageTask} object.
         *
         * @param bmImage is the ImageView for the particular thubnail
         */
        public DownloadImageTask(ImageView bmImage) {
            this.newsThumbnail = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            // store url string
            String urlDisplay = urls[0];
            //create Bitmap object
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e(getContext().getString(R.string.error), e.getMessage());
                e.printStackTrace();
            }
            //return Bitmap object
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //set thumbnail
            newsThumbnail.setImageBitmap(result);
        }
    }
}
