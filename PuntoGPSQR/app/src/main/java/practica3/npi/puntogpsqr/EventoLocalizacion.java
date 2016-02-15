package practica3.npi.puntogpsqr;

import com.google.android.gms.maps.model.LatLng;

public class EventoLocalizacion {
    public final LatLng localizacion;
    public final String tiempo;

    public EventoLocalizacion(LatLng localizacion, String tiempo) {
        this.localizacion = localizacion;
        this.tiempo = tiempo;
    }
}
