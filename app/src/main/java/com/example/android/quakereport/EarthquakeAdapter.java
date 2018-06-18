package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import android.graphics.drawable.GradientDrawable;

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
        final Earthquake currentEarthquake = getItem(position);

        // Create new Date object from  the current earthquakes time in milliseconds
        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());


        // Find the eathquake's magnitude TextView by it's ID earthquake_mag
        TextView mag = (TextView) listItemView.findViewById(R.id.earthquake_mag);


        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMag());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        // Format the current earthquake getMag as a string
        String formattedMag = formatMagnitude(currentEarthquake.getMag());
        //and set that number to the  mag TextView as a string
        mag.setText(formattedMag);


        // Find the eathquake's location offset TextView by it's ID earthquake_location_offset
        TextView locationOffset = (TextView) listItemView.findViewById(R.id.earthquake_location_offset);

        // Find the eathquake's primaryLocation TextView by it's ID earthquake_primary_location
        TextView primaryLocation = (TextView) listItemView.findViewById(R.id.earthquake_primary_location);

        //Create an empty string for the primary location
        String primaryLocationString;

        // Check if earthquake's place starts with an number
        // that would mean that it provides the distance from a major city or country
        if (Character.isDigit(currentEarthquake.getPlace().charAt(0))) {

            // Get the location offset string from the {@link Earthquake} getPlace method
            // By starting the string at index 0, and ending it after the word "of" (i.e 38km SE of)
            String locationOffsetString = currentEarthquake.getPlace().substring(0,
                    currentEarthquake.getPlace().indexOf("of") + 2);

            // Get the primary location string by erasing the locationOffsetString from
            // the {@link Earthquake} getPlace method string, so that it only shows a city or country
            primaryLocationString = currentEarthquake.getPlace().replace(locationOffsetString,
                    "").trim();

            // set the earthquake's location offset, and set it to the location offset TextView
            locationOffset.setText(locationOffsetString);
        } else {
            // Check if earthquake's place does NOT start with an number
            // which means it only displays a major city or country

            // Set the location offset TextView text to "Near"
            locationOffset.setText(R.string.near);

            // Use the getPlace method to get the location, and set it to the primaryLocationString
            primaryLocationString = currentEarthquake.getPlace();
        }

        // Get the earthquake's primary location, and set it to the TextView
        primaryLocation.setText(primaryLocationString);


        // Find the date of the eathquake TextView by it's ID earthquake_date
        TextView date = (TextView) listItemView.findViewById(R.id.earthquake_date);

        // Format the date string (i.e March 1, 2018)
        String formattedDate = formatDate(dateObject);

        // Get the current earthquake's date, and add it to the TextView
        date.setText(formattedDate);

        // Find the time of the eathquake TextView by it's ID earthquake_time
        TextView time = (TextView) listItemView.findViewById(R.id.earthquake_time);

        // Format time string (i.e 3:30 PM)
        String formattedTime = formatTime(dateObject);

        // Get the current earthquake's date, and add it to the TextView
        time.setText(formattedTime);

        // Return the whole list item layout so that it can be shown in the ListView
        return listItemView;
    }


    /**
     * @return formatted magnitude string showing 1 decimal place (i.e 3.4)
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat format = new DecimalFormat("0.0");
        return format.format(magnitude);
    }


    /**
     * @return the color that corresponds with the magnitude of the earthquake
     * ranging from blue (low magnitude) to red (high magnitude)
     */
    private int getMagnitudeColor(double magnitude) {

        //Convert the magnitude which is a double, into an int
        int magnitudeFloor = (int) Math.floor(magnitude);

        int magnitudeColor;

        switch (magnitudeFloor) {

            case 0:
            case 1:
                magnitudeColor = R.color.magnitude1;
                break;

            case 2:
                magnitudeColor = R.color.magnitude2;
                break;

            case 3:
                magnitudeColor = R.color.magnitude3;
                break;

            case 4:
                magnitudeColor = R.color.magnitude4;
                break;

            case 5:
                magnitudeColor = R.color.magnitude5;
                break;

            case 6:
                magnitudeColor = R.color.magnitude6;
                break;

            case 7:
                magnitudeColor = R.color.magnitude7;
                break;

            case 8:
                magnitudeColor = R.color.magnitude8;
                break;

            case 9:
                magnitudeColor = R.color.magnitude9;
                break;

            default:
                magnitudeColor = R.color.magnitude10plus;

        }

        return ContextCompat.getColor(getContext(), magnitudeColor);
    }

    /**
     * @return formatted date (i.e March 4th, 2012) string from a Date Object
     */
    private String formatDate(Date dateObject) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }


    /**
     * @return formatted time (i.e 3:35pm) string from a Date Object
     */
    private String formatTime(Date dateObject) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

}
