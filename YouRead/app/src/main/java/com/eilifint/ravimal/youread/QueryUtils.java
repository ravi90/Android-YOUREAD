package com.eilifint.ravimal.youread;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving news data from Guardian.
 */
public final class QueryUtils {
    static Context mContext;


    private QueryUtils() {
    }


    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Query the Guardian dataset and return an {@link News} object to represent a single news.
     */
    public static ArrayList<News> fetchNewsData(String requestUrl, Context context) {
        // Create URL object
        mContext = context;
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, context.getString(R.string.error_in_stream), e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<News> news = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.error_in_url), e);

        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        // url connection
        HttpURLConnection urlConnection = null;
        // data stream
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod(mContext.getString(R.string.get));
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, mContext.getString(R.string.error_respond) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.problem), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName(mContext.getString(R.string.utf8)));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link News} object by parsing out information
     * about the first news from the input newsJSON string.
     */
    private static ArrayList<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();
        try {


            // build up a list of News objects with the corresponding data.

            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            if (baseJsonResponse.getJSONObject(mContext.getString(R.string.response)).
                    has(mContext.getString(R.string.results))) {
                JSONArray arrayResults = baseJsonResponse.getJSONObject(mContext.getString(R.string.response)).
                        getJSONArray(mContext.getString(R.string.results));


                for (int i = 0; i < arrayResults.length(); i++) {
                    String webTitle = mContext.getString(R.string.not_available);
                    String webUrl = mContext.getString(R.string.not_available);
                    String thumbnailUrl = mContext.getString(R.string.not_available);
                    String trailText = mContext.getString(R.string.not_available);
                    String webPublicationDate = mContext.getString(R.string.not_available);
                    String author = mContext.getString(R.string.unknow);

                    if (arrayResults.getJSONObject(i).has(mContext.getString(R.string.web_title)))
                        webTitle = arrayResults.getJSONObject(i).getString(mContext.getString(R.string.web_title));

                    if (arrayResults.getJSONObject(i).has(mContext.getString(R.string.web_url)))
                        webUrl = arrayResults.getJSONObject(i).getString(mContext.getString(R.string.web_url));

                    if (arrayResults.getJSONObject(i).has(mContext.getString(R.string.web_date)))
                        webPublicationDate = arrayResults.getJSONObject(i).getString(mContext.getString(R.string.web_date)).substring(0, 10);

                    if (arrayResults.getJSONObject(i).getJSONArray(mContext.getString(R.string.reference)).length() > 0)
                        author = arrayResults.getJSONObject(i).getJSONArray(mContext.getString(R.string.reference)).
                                getJSONObject(0).getString(mContext.getString(R.string.id));

                    if (arrayResults.getJSONObject(i).has(mContext.getString(R.string.fields_json)) && arrayResults.getJSONObject(i).
                            getJSONObject(mContext.getString(R.string.fields_json)).has(mContext.getString(R.string.trailText))) {
                        trailText = arrayResults.getJSONObject(i).getJSONObject(mContext.getString(R.string.fields_json)).
                                getString(mContext.getString(R.string.trailText));
                    }

                    if (arrayResults.getJSONObject(i).has(mContext.getString(R.string.fields_json)) && arrayResults.getJSONObject(i).
                            getJSONObject(mContext.getString(R.string.fields_json)).has(mContext.getString(R.string.thumbnail))) {
                        thumbnailUrl = arrayResults.getJSONObject(i).getJSONObject(mContext.getString(R.string.fields_json)).
                                getString(mContext.getString(R.string.thumbnail));
                    }

                    // create a new {@link Book} News
                    News current = new News(webTitle, webUrl, trailText, thumbnailUrl, author, webPublicationDate);
                    news.add(current);

                }
                // return ArrayList
                return news;
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, mContext.getString(R.string.problem_parsing_result), e);
        }

        return null;
    }

}