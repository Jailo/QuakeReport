package com.example.android.quakereport;

public class Earthquake {

    /* Magnitute of the earthquake */
    private Double mMag;

    /* Place where the earthquake happened */
    private String mPlace;

    /* Date of when the earthquake happened */
    private String mDate;


    /**
     * Creates a new earthquake object
     * @param mag is the magnitude of the earthquake
     * @param place where the earthquake happened
     * @param date when the earthquake happened
     */
    public Earthquake(Double mag, String place, String date) {
        mMag = mag;
        mPlace = place;
        mDate = date;

    }

    /**
     *
     * @return the magnitude of the earthquake
     */
    public Double getMag() { return mMag; }


    /**
     *
     * @return the place where the earthquake happened
     */
    public String getPlace() { return mPlace; }


    /**
     *
     * @return the date when the earthquake happened
     */
    public String getDate() { return mDate; }


}
