package nbsolution.muslim.app.Helper;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import nbsolution.muslim.app.Constants;
import nbsolution.muslim.app.utils.PrefsUtils;

public class GpsTracker extends Service implements LocationListener {

    private static final String LOG_TAG = GpsTracker.class.getSimpleName();
    private PrefsUtils utils;
    private Context mContext;

    public GpsTracker() {
    }

    public GpsTracker(Context context) {
        this.mContext = context;
        utils = new PrefsUtils(mContext);
        getLocation();
    }

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand()");
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            Log.d("flowcheckone", "try");
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            utils.saveToPrefs(Constants.LATITUDE, latitude);
                            utils.saveToPrefs(Constants.LONGITUDE, longitude);
                            sendLocalBroadCast(longitude, latitude);
                            Log.d("flowcheckoneone", "Your Network Location is - \nLat: "+latitude+"\nLong: "+longitude);
                        }
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    Log.d("flowcheckoneone", "if second");
                    if (location == null) {
                        if (locationManager != null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        }
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                utils.saveToPrefs(Constants.LATITUDE, latitude);
                                utils.saveToPrefs(Constants.LONGITUDE, longitude);
                                sendLocalBroadCast(longitude, latitude);
                                Log.d("flowcheckoneone", "Your Gps Location is - \nLat: "+latitude+"\nLong: "+longitude);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            Log.d("flowcheckone", e.toString());
            e.printStackTrace();
        }

        return location;
    }

    private void sendLocalBroadCast(double longitude,double latitude) {
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent();
        intent.setAction("notice");
        intent.putExtra("lat", latitude);
        intent.putExtra("longt", longitude);
        localBroadcastManager.sendBroadcast(intent);
    }



    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app.
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsTracker.this);
        }
    }


    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onProviderDisabled(String provider) {
    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}