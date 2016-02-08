package practica3.npi.puntomovimientosonido;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/*
 * Clase que maneja el acelerómetro.
 */
public class AcelerometroData implements SensorEventListener {
    private final SensorManager sensorManager;
    private final Sensor acelerometro;
    private final MainActivity mainActivity;

    private float[] datosAcelerometro;
    private float[] gravedad;
    private float[] aceleracionLineal;
    private final float filtroAlpha = 0.8f;
    // TODO fijar el siguiente valor bien
    private final float aceleracionMinima = 3;

    /*
     * Inicializamos los valores que se le pasan en la actividad MainActivity:
     *  - sensorManager no se iniciliza aquí porque tiene que llamar a getSystemService(),
     *    que esta disponible solo en el contexto (esto es, en la actividad MainActivity)
     *  - mainActivity es necesario para llamar a funciones que están definidas allí
     */
    public AcelerometroData(SensorManager sensorManager, MainActivity mainActivity) {
        this.sensorManager = sensorManager;
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mainActivity = mainActivity;

        datosAcelerometro = null;
        gravedad = new float[3];
        aceleracionLineal = new float[3];
    }

    protected void onResume() {
        // Registramos un listerner para recibir datos del acelerómetro del dispositivo
        sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        // Una buena práctica es parar los sensores cuando la aplicación no está en primer plano
        sensorManager.unregisterListener(this);
    }

    // Esta función se ejecuta cada vez que el sensor cambia
    public void onSensorChanged(SensorEvent evento) {
        // Solo nos interesa cuando el evento esta asociado al acelerómetro
        if (evento.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            datosAcelerometro = evento.values;
        }

        if ((datosAcelerometro != null)) {
            /*
             * Isolamos la fuerza de la gravedad con el filtro paso-bajo (alpha).
             * Alpha se ha calculado como t/(t+dT), donde t es el filtro paso-bajo
             * constante en el tiempo y dT es la tasa de envíos de eventos.
             */
            gravedad[0] = filtroAlpha * gravedad[0] + (1 - filtroAlpha) * evento.values[0];
            gravedad[1] = filtroAlpha * gravedad[1] + (1 - filtroAlpha) * evento.values[1];
            gravedad[2] = filtroAlpha * gravedad[2] + (1 - filtroAlpha) * evento.values[2];

            // Removemos la contribución de la gravedad con el filtro paso-alto
            aceleracionLineal[0] = evento.values[0] - gravedad[0];
            aceleracionLineal[1] = evento.values[1] - gravedad[1];
            aceleracionLineal[2] = evento.values[2] - gravedad[2];

            /*
             * Si agitamos el dispositivo en la dirección de la X,
             * gravedad[0] contendrá un valor positivo no trivial
             * y utilizamos este valor para detectar el gesto que
             * activará el sonido
             */
            // TODO PERFILAR CONDICION
            mainActivity.fijarTextoAceleracion(aceleracionLineal[0]);
            if (aceleracionLineal[0] > aceleracionMinima){
                mainActivity.reproducirSonidoYAnimacion();
            }
        }
    }

    // Esta función necesita estar definida aunque no es necesario implementarla en nuestro caso
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}