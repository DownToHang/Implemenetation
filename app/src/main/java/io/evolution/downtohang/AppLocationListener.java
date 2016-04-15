package io.evolution.downtohang;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

class AppLocationListener implements LocationListener {

    private Location location;

    public Location getLocation() {
        return location;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            this.location = location;
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        System.out.println("AppLocationListener Enabled!");
    }

    @Override
    public void onProviderDisabled(String provider) {
        System.out.println("AppLocationListener Disabled!");
    }

}