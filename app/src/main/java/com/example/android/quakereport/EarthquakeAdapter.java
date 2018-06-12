package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    /**
     * Custom array adapter. the context is used to inflate the layout file and the list is the data
     * we want to populate into the list
     * @param context the current context, used to inflate the layout file
     * @param earthquakes a list of Earthquake objects to display in a list
     */
    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Check if existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }

        // Get the {@link Earthquake} object located at this position in the list
        Earthquake currentEarthquake = getItem(position);

        // Find the eathquake's magnitude TextView by it's ID earthquake_mag
        TextView mag = (TextView) listItemView.findViewById(R.id.earthquake_mag);
        // Get the current earthquake's mag, and set that number to the TextView as a string
        mag.setText(currentEarthquake.getMag().toString());

        // Find the eathquake's place TextView by it's ID earthquake_place
        TextView place = (TextView) listItemView.findViewById(R.id.earthquake_place);
        // Get the current earthquake's place, and set it to the TextView
        place.setText(currentEarthquake.getPlace());


        // Find the date the eathquake TextView by it's ID earthquake_date
        TextView date = (TextView) listItemView.findViewById(R.id.earthquake_date);
        // Get the current earthquake's date, andit to the TextView
        date.setText(currentEarthquake.getDate());


        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }
}
