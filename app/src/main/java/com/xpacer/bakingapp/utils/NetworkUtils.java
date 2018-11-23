package com.xpacer.bakingapp.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.xpacer.bakingapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {
    private static final String BAKING_JSON_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    /**
     * Insert API Key Here
     */
    private static final String API_KEY = "";

    public static URL bakingUrl() {
        Uri recipesQueryUri = Uri.parse(BAKING_JSON_URL).buildUpon()
                .build();

        try {
            return new URL(recipesQueryUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Method to check if the device is connected to the internet
     *
     * @param context : Context
     * @return boolean
     */
    private static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        return cm != null && cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();
    }

    public interface QueryResult {
        void onSuccess(String result);
    }

    @SuppressLint("StaticFieldLeak")
    public static class ApiCallerTask extends AsyncTask<Void, Void, String> {
        private final Context mContext;
        private View mProgressBar;
        private QueryResult mQueryResult;
        private URL bakingUrl;

        public ApiCallerTask(Context context, QueryResult queryResult, View progressBar) {
            mContext = context;
            mProgressBar = progressBar;
            mQueryResult = queryResult;
            bakingUrl = NetworkUtils.bakingUrl();
        }


        @Override
        protected void onPreExecute() {
            // SHOW LOADER
            if (!NetworkUtils.isConnected(mContext)) {
                Toast errorToast = Toast.makeText(mContext, mContext.getString(R.string.no_internet_message),
                        Toast.LENGTH_LONG);
                errorToast.show();
                cancel(true);
                return;
            }

            if (bakingUrl == null) {
                cancel(true);
                return;
            }

            if (mProgressBar != null)
                mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String queryResults = null;

            try {
                queryResults = NetworkUtils.getResponseFromHttpUrl(bakingUrl);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException je) {
                je.printStackTrace();
            }

            return queryResults;
        }

        @Override
        protected void onPostExecute(String result) {
            if (mProgressBar != null)
                mProgressBar.setVisibility(View.GONE);

            if (result == null) {
                Toast errorToast = Toast.makeText(mContext,
                        mContext.getString(R.string.something_went_wrong), Toast.LENGTH_LONG);
                errorToast.show();
                return;
            }

            mQueryResult.onSuccess(result);
        }

    }
}
