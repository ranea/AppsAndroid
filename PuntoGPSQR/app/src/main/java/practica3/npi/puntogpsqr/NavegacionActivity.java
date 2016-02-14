package practica3.npi.puntogpsqr;


import android.content.Intent;

import android.net.Uri;
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
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;

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

    private double latitud, longitud;

    private GoogleMap mapa;
    private ArrayList<Location> mListLocations;

    private boolean mRequestingLocationUpdates = true;

    private Intent mapIntent = new Intent(); //Intent que lanza la aplicación de GPS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);

        Intent intent = getIntent();
        String mensaje = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        obtenerCoordenadas(mensaje);

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

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Double.toString(latitud) + "," + Double.toString(longitud) + "&mode=w");

        //Lanzamos Google Maps con dicha petición
        mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);


    }

    public void obtenerCoordenadas(String textoCoordenadas) {
        textoCoordenadas = textoCoordenadas.replace("LATITUD_", "");
        textoCoordenadas = textoCoordenadas.replace("_LONGITUD_", " ");
        String[] coordinates = textoCoordenadas.split(" ");
        latitud = Double.parseDouble(coordinates[0]);
        longitud = Double.parseDouble(coordinates[1]);
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
            Log.d("GPSQR", "Añadiendo localización");
            LatLng actual = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            mapa.addMarker(new MarkerOptions().title("Punto " + mListLocations.size()).position(actual));

            if (mListLocations.size() >= 2){
                Location loc_anterior = mListLocations.get(mListLocations.size() - 2);
                LatLng anterior = new LatLng(loc_anterior.getLatitude(), loc_anterior.getLongitude() ); // Tomar el anterior, el actual es - 1

                mapa.addPolyline(new PolylineOptions().add(anterior, actual)
                        .width(10)
                        .color(Color.RED));
            }
        }
        else
            Log.d("GPSQR", "No hay localización!");
    }
}
