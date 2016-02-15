package practica3.npi.puntogpsqr;

/**
 * Created by Antonio on 15/02/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;


public class LocationService extends Service implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {
    private static final String TAG = LocationService.class.getSimpleName();

    public static final String BROADCAST_ACTION = "practica3.npi.puntogpsqr.LOCATION_UPDATE";

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 60;

    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 40;

    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;

    private boolean mRequestingLocationUpdates = true;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Location service created…");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (mLocationRequest == null) {
            createLocationRequest();
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

//    // Unregister location listeners
//    private void clearLocationData() {
//        locationClient.disconnect();
//
//        if (locationClient.isConnected()) {
//            locationClient.removeLocationUpdates(this);
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


//    // When service destroyed we need to unbind location listeners
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        Log.d(TAG, "Location service destroyed…");
//
//        clearLocationData();
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Calling command…");

        return START_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle) {
//        Log.d(TAG, "Location Callback. onConnected");
//
//        Location currentLocation = locationClient.getLastLocation();
//
//        // Create the LocationRequest object
//        LocationRequest locationRequest = LocationRequest.create();
//
//        // Use high accuracy
//        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//        // Set the update interval to 5 seconds
//        locationRequest.setInterval(UPDATE_INTERVAL);
//
//        // Set the fastest update interval to 1 second
//        locationRequest.setFastestInterval(FASTEST_INTERVAL);
//
//        locationClient.requestLocationUpdates(locationRequest, this);
//
//        onLocationChanged(currentLocation);

        if (mRequestingLocationUpdates) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        Log.d(TAG, "LOCATION: " + location.getLatitude() + ":" + location.getLongitude());
//
//        // Since location information updated, broadcast it
//        Intent broadcast = new Intent();
//
//        // Set action so other parts of application can distinguish and use this information if needed
//        broadcast.setAction(BROADCAST_ACTION);
//        broadcast.putExtra("latitude", location.getLatitude());
//        broadcast.putExtra("longitude", location.getLongitude());
//
//        sendBroadcast(broadcast);

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        LatLng loc = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        EventoLocalizacion evento = new EventoLocalizacion(loc, mLastUpdateTime);
    }

//    @Override
//    public void onDisconnected() {
//        Log.d(TAG, "Location Callback. onDisconnected");
//    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Location Callback. onConnectionFailed");
    }
}