/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * Url for earthquake data from USGS dataset
     */
    private static final String USGS_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    /** Constant value for Loader ID
     * Assigning Explicit ID integers only comes into play when there are multiple Loaders
     * on the same Activity. So doing this is redundant, but it was on the course so I'll do it anyway
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    /** Adapter for list of earthquakes*/
    private EarthquakeAdapter mAdapter;

    /**
     * Text view that is displayed if there are no earthquakes to display
     */
    private TextView mEmptyStateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Find reference to the no earthquakes text view in the layout
        mEmptyStateView = (TextView) findViewById(R.id.no_earthquakes);
        // Set the earthquakeListView's empty state view to mEmptyStateView
        earthquakeListView.setEmptyView(mEmptyStateView);

        // Query the active network and determine if it has Internet connectivity.
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // If users phone is connected to the internet
        if (isConnected){
            // Get reference to loader manager to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initialize loader. Pass in the loader int id, null for the bundle, and this activity for
            // the loader callbacks so that the data returned comes back here
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
        } else {
            // If the user is NOT connected to the internet,
            // set mEmptyStateView text to "No Internet Connection"
            mEmptyStateView.setText(R.string.no_internet);

            // Find resource for progress bar
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            // and make it invisible
            progressBar.setVisibility(View.GONE);

        }


        // Create a new {@link EarthquakeArrayAdapter} that takes an empty list as input
        mAdapter = new EarthquakeAdapter(getBaseContext(), new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Find the current earthquake that was clicked on
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create new Intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send intent to start new activity
                startActivity(websiteIntent);

            }
        });

    }


    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {

        // Get shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Create a minimum magnitude string by getting the key and value from the shared preferences
        String minMagnitude = sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        // create an order by string that holds the selected order by preference option
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        // create an maximum results string that holds the maximum result preference option
        String maxResults = sharedPreferences.getString(
                getString(R.string.settings_max_results_key),
                getString(R.string.settings_max_results_default));

        // create a base uri in order to parse through the basic USGS_URL queury string
        Uri baseUri = Uri.parse(USGS_URL);

        // Create a uri builder to add query parameter strings onto the base uri
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Add the following search params onto uri builder
        // format to geojson, list item limit is maxResults, minimum magnitude to minMagnitude string,
        // and order by orderBy preference string
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", maxResults);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        // Create new loader for the given URL
        return new EarthquakeLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        // Find resource for, and hide progress bar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        //set mEmptyStateView text to "No Earthquakes Found"
        mEmptyStateView.setText(R.string.no_earthquakes);

        // If there is a valid list of {@link Earthquake}s, add all data to the adapter.
        // This will cause the ListView to update
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }

    }


    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        // Clear the adapter of all data
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the Menu
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get items Id
        int id = item.getItemId();

        // Check if id is equal to action_settings icon
        if (id == R.id.action_settings){
            //Create and start intent to settings activity
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

