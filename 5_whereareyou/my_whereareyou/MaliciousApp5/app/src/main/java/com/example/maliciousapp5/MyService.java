package com.example.maliciousapp5;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MyService extends Service {

    private static final String TAG = "MOBIOTSEC";

    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f; // minDistanceM float: minimum distance between location updates in meters

    private MyLocationListener[] mLocationListeners = new MyLocationListener[] {
            new MyLocationListener(LocationManager.GPS_PROVIDER),
            new MyLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initLocationManager(){
        // initialize location manager
        if (this.locationManager == null) {
            this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initLocationManager();

        //try to get position from GPS_PROVIDER
        try{
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"Error: No permissions");
            } else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);
            }
        }catch(Exception e){
            Log.e(TAG,"Error: onCreate - GPS_PROVIDER: \n" + e);
        }

        // try to get position from NETWORK_PROVIDER
        try{
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG,"Error: No permissions");
            } else{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);
            }
        }catch(Exception e){
            Log.e(TAG, "Error: onCreate - NETWORK_PROVIDER: \n" + e);
        }

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationListeners!= null){
            try{
                for(int i=0; i < mLocationListeners.length; i++) {
                    locationManager.removeUpdates(mLocationListeners[0]);
                    locationManager.removeUpdates(mLocationListeners[1]);
                }
            }catch(Exception e){
                Log.e(TAG, "Exception: onDestroy - " + e);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) { //it is called by the system after the invocation of startService() or startForegroundService() by an activity or other application component by intent
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        Location mLastLocation;

        public MyLocationListener(String provider) {
            super();
            Log.i(TAG, "MyLocationListner constructor: " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location loc) {
            Log.i(TAG, "onLocationChanged: " + loc);
            mLastLocation.set(loc);
            Intent i = new Intent();
            i.setAction("com.mobiotsec.intent.action.LOCATION_ANNOUNCEMENT");
            i.putExtra("location",loc);
            sendBroadcast(i);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
        }
    }

}