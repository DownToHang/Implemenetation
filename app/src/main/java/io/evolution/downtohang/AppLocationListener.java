package io.evolution.downtohang;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

class AppLocationListener implements LocationListener {

    private double latitude;
    private double longitude;

    public AppLocationListener(double startLat, double startLong) {
        latitude = startLat;
        longitude = startLong;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("New Lat Long!");
        if(location != null) {
            latitude = location.getLongitude();
            longitude = location.getLatitude();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}