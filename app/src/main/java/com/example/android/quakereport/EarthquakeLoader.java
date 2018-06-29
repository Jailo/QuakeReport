package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Returns a list of Earthquakes by using AsyncTask to preform a http request on the given URL
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    private String mUrls;

    public EarthquakeLoader(Context context, String urls) {
        super(context);
        mUrls = urls;
    }

    @Override
    public List<Earthquake> loadInBackground() {
        // Checks if there is no url or if the url is null
        // Then there is no data to fetch from the internet. Do nothing
        if (mUrls == null) {
            return null;
        }

        // Create a list of {@link Earthquake}s.
        List<Earthquake> result = QueryUtils.fetchEarthquakeData(mUrls);
        return result;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
