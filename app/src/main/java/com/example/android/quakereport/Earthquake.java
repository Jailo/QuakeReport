package com.example.android.quakereport;

public class Earthquake {

    /* Magnitute of the earthquake */
    private Double mMag;

    /* Place where the earthquake happened */
    private String mPlace;

    /* Time of the earthquake */
    private Long mTimeInMilliseconds;


    /**
     * Creates a new earthquake object
     * @param mag is the magnitude of the earthquake
     * @param place where the earthquake happened
     * @param timeInMilliseconds when the earthquake happened
     */
    public Earthquake(Double mag, String place, Long timeInMilliseconds) {
        mMag = mag;
        mPlace = place;
        mTimeInMilliseconds = timeInMilliseconds;

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


}
