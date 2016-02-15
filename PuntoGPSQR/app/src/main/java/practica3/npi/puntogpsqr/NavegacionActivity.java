package practica3.npi.puntogpsqr;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;


import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.graphics.Color;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NavegacionActivity extends FragmentActivity  {
    // TODO change name
    private static final int NAVIGATION_REQUEST_CODE = 100;

    private double latitud, longitud;

    private GoogleMap mapa;
    private ArrayList<EventoLocalizacion> mListLocations;


    private Intent mapIntent = new Intent(); //Intent que lanza la aplicación de GPS
    private Intent LocService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);

        Intent intent = getIntent();
        String mensaje = intent.getStringExtra(MainActivity.EXTRA_MENSAJE);

        obtenerCoordenadas(mensaje);

        EventBus.getDefault().register(this);
        LocService = new Intent(this, LocationService.class);
        startService(LocService);

        mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
        mListLocations = new ArrayList<>();

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Double.toString(latitud) + "," + Double.toString(longitud) + "&mode=w");

        //Lanzamos Google Maps con dicha petición
        mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivityForResult(mapIntent,NAVIGATION_REQUEST_CODE);


    }

    public void obtenerCoordenadas(String textoCoordenadas) {
        textoCoordenadas = textoCoordenadas.replace("LATITUD_", "");
        textoCoordenadas = textoCoordenadas.replace("_LONGITUD_", " ");
        String[] coordinates = textoCoordenadas.split(" ");
        latitud = Double.parseDouble(coordinates[0]);
        longitud = Double.parseDouble(coordinates[1]);
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NAVIGATION_REQUEST_CODE) {
                EventoLocalizacion actual;
                ArrayList<LatLng> localizaciones = new ArrayList<>();
                for (int i = 0; i < mListLocations.size(); i++) {
                    actual = mListLocations.get(i);
                    mapa.addMarker(new MarkerOptions().title("Punto " + Integer.toString(i) + " " + actual.tiempo).position(actual.localizacion));
                    localizaciones.add(actual.localizacion);
                }

                mapa.addPolyline(new PolylineOptions().addAll(localizaciones).color(Color.RED));
                stopService(LocService);
        }

    }

    private String getDateTime() {
        // get date time in custom format
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
        return sdf.format(new Date());
    }


    // This method will be called when a MessageEvent is posted
    @Subscribe
    public void onEventoLocalizacion(EventoLocalizacion evento){
        mListLocations.add(evento);
        Log.w("NavegacionActivity", "Evento Recibido");
        Toast.makeText(this, "Localizacion recibida: " + evento.localizacion.toString(), Toast.LENGTH_SHORT).show();
    }

}
