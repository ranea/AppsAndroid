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

import org.greenrobot.eventbus.EventBus;

import java.text.DateFormat;
import java.util.Date;


public class ServicioLocalizacion extends Service implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {
    private static final String TAG = ServicioLocalizacion.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private Location localizacionActual;
    private LocationRequest peticionLocalizacion;
    private String ultimaActualizacion;

    private boolean pidiendoDatos = true;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.w(TAG, "Location service created…");

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Log.w(TAG, "CREATED");
        }


    }

    /**
     * Crear petición de localización de los servicios de Google.
     */
    protected void crearPeticionLocalizacion() {
        Log.w(TAG, "Created Location Request");
        peticionLocalizacion = new LocationRequest();
        peticionLocalizacion.setInterval(10000);
        peticionLocalizacion.setFastestInterval(5000);
        peticionLocalizacion.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "Calling command…");
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.w(TAG, "Location Callback. onConnected");

        if (peticionLocalizacion == null) {
            crearPeticionLocalizacion();
        }


        if (pidiendoDatos) {
            Log.w(TAG,"Enabling location updates");
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, peticionLocalizacion, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.w(TAG, "onLocationChanged");
        Log.w(TAG, "LOCATION: " + location.getLatitude() + ":" + location.getLongitude());

        localizacionActual = location;
        ultimaActualizacion = DateFormat.getTimeInstance().format(new Date());

        LatLng loc = new LatLng(localizacionActual.getLatitude(), localizacionActual.getLongitude());

        EventoLocalizacion evento = new EventoLocalizacion(loc, ultimaActualizacion);

        EventBus.getDefault().post(evento);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w(TAG, "Location Callback. onConnectionFailed");
    }
}