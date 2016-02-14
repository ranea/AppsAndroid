package practica3.npi.puntogpsqr;


import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Location;

import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.util.Log;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationListener;


import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NavegacionActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
    // TODO change name
    private static final int INTENT_V1 = 1;

    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private String mLastUpdateTime;

    private GoogleMap mapa;
    private ArrayList<Location> mListLocations;

    private boolean mRequestingLocationUpdates = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);

        // Create an instance of GoogleAPIClient.
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

        mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
        mListLocations = new ArrayList<>();

//        updateValuesFromBundle(savedInstanceState);


    }

//    private void updateValuesFromBundle(Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            // Update the value of mRequestingLocationUpdates from the Bundle, and
//            // make sure that the Start Updates and Stop Updates buttons are
//            // correctly enabled or disabled.
//            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
//                mRequestingLocationUpdates = savedInstanceState.getBoolean(
//                        REQUESTING_LOCATION_UPDATES_KEY);
//                setButtonsEnabledState();
//            }
//
//            // Update the value of mCurrentLocation from the Bundle and update the
//            // UI to show the correct latitude and longitude.
//            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
//                // Since LOCATION_KEY was found in the Bundle, we can be sure that
//                // mCurrentLocationis not null.
//                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
//            }
//
//            // Update the value of mLastUpdateTime from the Bundle and update the UI.
//            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
//                mLastUpdateTime = savedInstanceState.getString(
//                        LAST_UPDATED_TIME_STRING_KEY);
//            }
//            updateUI();
//        }

    @Override
    public void onConnected(Bundle connectionHint) {
//        Location currentPos = LocationServices.​FusedLocationApi.getLastLocation(​mGoogleApiClient​​);
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed (ConnectionResult result) {

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
//                mRequestingLocationUpdates);
//        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
//        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
//        super.onSaveInstanceState(savedInstanceState);
//    }

    private String getDateTime() {
        // get date time in custom format
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
        return sdf.format(new Date());
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        mListLocations.add(mCurrentLocation);
        updateUI();
    }

//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }

    public void updateUI() {
        if (mCurrentLocation != null) {
            LatLng tmp = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//            TextView mLatitudeText = (TextView) findViewById(R.id.mLatitudeText);
//            mLatitudeText.setText(String.valueOf(mCurrentLocation.getLatitude()));
//            TextView mLongitudeText = (TextView) findViewById(R.id.mLongitudeText);
//            mLongitudeText.setText(String.valueOf(mCurrentLocation.getLongitude()));
//
//            TextView mLastUpdateTimeTextView = (TextView) findViewById(R.id.mLastUpdateTimeTextView);
//            mLastUpdateTimeTextView.setText(mLastUpdateTime);

            Log.w("GPSQR", "Añadiendo localización");
            mapa.addMarker(new MarkerOptions().title("Punto ").position(tmp));
        }
        else
            Log.w("GPSQR", "No hay localización!");
    }
}
