package com.example.android.quakereport;

public class Earthquake {

    /* Magnitute of the earthquake */
    private Double mMag;

    /* Place where the earthquake happened */
    private String mPlace;

    /* Time of the earthquake */
    private Long mTimeInMilliseconds;

    /* Url link to find more info about the earthquake */
    private String mUrl;

    /**
     * Creates a new earthquake object
     * @param mag is the magnitude of the earthquake
     * @param place where the earthquake happened
     * @param timeInMilliseconds when the earthquake happened
     * @param url the url that leads to more info about the earthquake
     */
    public Earthquake(Double mag, String place, Long timeInMilliseconds, String url) {
        mMag = mag;
        mPlace = place;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;

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
     * @return the time when the earthquake happened
     */
    public Long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }


    /**
     * @return the url link to more info about the earthquake
     */
    public String getUrl() {
        return mUrl;
    }

}
