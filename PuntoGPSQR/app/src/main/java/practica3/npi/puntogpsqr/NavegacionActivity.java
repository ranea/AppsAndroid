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

/**
 * Activity que lleva el dibujado de los puntos por donde se va pasando.
 */
public class NavegacionActivity extends FragmentActivity  {
    private static final int NAVIGATION_REQUEST_CODE = 100;

    private double latitud, longitud;

    private GoogleMap mapa;
    private ArrayList<EventoLocalizacion> listaLocalizaciones;


    private Intent mapIntent = new Intent(); //Intent que lanza la aplicación de GPS
    private Intent LocService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);

        Intent intent = getIntent();
        String mensaje = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        obtenerCoordenadas(mensaje);

        // Registramos en el bus de comunicaciones
        EventBus.getDefault().register(this);

        // Lanzamos el servicio de localización
        LocService = new Intent(this, ServicioLocalizacion.class);
        startService(LocService);

        // Obtenemos el mapa desde el layout
        mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
        listaLocalizaciones = new ArrayList<>();

        // Uri para llamar a la navegación de google maps hacia las coordenadas indicadas
        Uri uriNavegacion = Uri.parse("google.navigation:q=" + Double.toString(latitud) + "," + Double.toString(longitud) + "&mode=w");

        // Lanzamos Google Maps con la petición de navegación
        mapIntent = new Intent(Intent.ACTION_VIEW, uriNavegacion);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivityForResult(mapIntent,NAVIGATION_REQUEST_CODE);


    }

    /**
     * Obtener coordenadas desde un string del QR
     * @param textoCoordenadas Texto desde QR
     */
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
                for (int i = 0; i < listaLocalizaciones.size(); i++) {
                    actual = listaLocalizaciones.get(i);
                    mapa.addMarker(new MarkerOptions().title("Punto " + Integer.toString(i) + " " + actual.tiempo).position(actual.localizacion));
                    localizaciones.add(actual.localizacion);
                }

                mapa.addPolyline(new PolylineOptions().addAll(localizaciones).color(Color.RED));
                stopService(LocService);
        }

    }


    // This method will be called when a MessageEvent is posted

    /**
     * Callback para la gestión de eventos recibidos.
     * @param evento Evento recibido
     */
    @Subscribe
    public void onEventoLocalizacion(EventoLocalizacion evento){
        listaLocalizaciones.add(evento);
        Log.w("NavegacionActivity", "Evento Recibido");
        Toast.makeText(this, "Localizacion recibida: " + evento.localizacion.toString(), Toast.LENGTH_SHORT).show();
    }

}
