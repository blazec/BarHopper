package com.example.blaise.barhop;

import org.json.JSONObject;

/**
 * Created by blaise on 2015-01-31.
 */
abstract class NearbyPlace {

    private String mName;
    private double mDistance;
    private String mAddress;
    private boolean mOpenNow;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public boolean isOpenNow() {
        return mOpenNow;
    }

    public void setOpenNow(boolean openNow) {
        mOpenNow = openNow;
    }
}
