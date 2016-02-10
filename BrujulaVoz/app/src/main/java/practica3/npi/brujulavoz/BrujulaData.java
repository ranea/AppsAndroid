package practica3.npi.brujulavoz;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/*
 * Clase que maneja los sensores acelerómetro y magnetómetro.
 * Estos datos son usados para realizar la brújula.
 */
public class BrujulaData implements SensorEventListener {
    private final SensorManager sensorManager;
    private final Sensor acelerometro;
    private final Sensor magnetometro;
    private final BrujulaActivity brujulaActivity;

    private float[] datosAcelerometro;
    private float[] datosMagnetrometro;
    private float[] matrizRotacion;
    private float[] orientacion;
    private float orientacionAnterior;
    private float orientacionDada;
    private int margenError;

    /*
     * Inicializamos los valores que se le pasan en la actividad BrujulaActivity:
     *  - sensorManager no se iniciliza aquí porque tiene que llamar a getSystemService(),
     *    que esta disponible solo en el contexto (esto es, en la actividad BrujulaActivity)
     *  - brujulaActivity es necesario para llamar a funciones que están definidas allí
     */
    public BrujulaData(SensorManager sensorManager, BrujulaActivity brujulaActivity, String mensaje) {
        // Para poder utilizar los sensores, necesitamos antes un "sensorManager"
        // que manejará los sensores.
        this.sensorManager = sensorManager;
        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometro = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.brujulaActivity = brujulaActivity;

        datosAcelerometro = null;
        datosMagnetrometro = null;
        matrizRotacion = new float[9];
        orientacion = new float[3];
        orientacionAnterior = 180f;

        orientacionDada = calcularOrientacionDada(mensaje);
        margenError = calcularMargenError(mensaje);
    }

    protected void onResume() {
        // Registramos dos listeners para recibir datos de los sensores dentro del dispositivo
        sensorManager.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometro, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        // Una buena práctica es parar los sensores cuando la aplicación no está en primer plano
        sensorManager.unregisterListener(this);
    }

    // Esta función se ejecuta cada vez que el sensor cambia
    public void onSensorChanged(SensorEvent evento) {
        // Distinguimos el tipo de evento que se ha recibido (puede deberse al acelerómetro o al magnetómetro)
        switch (evento.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                datosAcelerometro = evento.values;
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                datosMagnetrometro = evento.values;
                break;
        }

        // Necesitamos datos de los dos sensores para crear la brújula
        // Si no se han recibido datos de ambos, no hacemos nada
        if ((datosAcelerometro != null) && (datosMagnetrometro != null)) {
            /*
             * Para calcular la orientación del dispositivo, calculamos el azimut:
             * "ángulo formado entre la dirección de referencia (norte) y una línea entre
             * el observador y un punto de interés previsto en el mismo plano que la dirección
             * de referencia" - Wikipedia.
             * Si el azimut es 0º, el dispositivo se encuentra orientado hacia el norte,
             * 90º para el este, 180º para el sur y 270º para el oeste.
             *
             * Para calcular el azitmut, primero debemos calcular la matriz de rotación
             * (con los datos del acelerómetro y magnetómetro) y después
             * usando dicha matriz obtenemos un vector cuya 1ª componente es el azimut.
             */
            SensorManager.getRotationMatrix(matrizRotacion, null, datosAcelerometro, datosMagnetrometro);
            SensorManager.getOrientation(matrizRotacion, orientacion);
            float azimut = orientacion[0];

            // Pasamos los azimut dados en radianes a grados
            float orientacionDispositivo = (float)(Math.toDegrees(azimut)+360)%360;

            // Mostramos por pantalla la orientación
            brujulaActivity.editarTextoOrientacionDispositivo(orientacionDispositivo);

            /*
             * Creamos una animación para que el puntero se mueva desde la orientación
             * anterior del dispositivo a la actual. Para ello utilizamos RotateAnimation.
             * Posteriormente en iniciarAnimacionPuntero() le aplicaremos
             * dicha animacion a la imagen del puntero.
             *
             * Aplicamos la animación siempre que no se tenga el caso de que se pase
             * de 359º a 1º (o viceversa) para evitar que la brújula haga giros innecesarios.
             */
            if (!((orientacionAnterior < -358 && orientacionDispositivo < 2 ) ||
                    (orientacionDispositivo > 358 && orientacionAnterior < 2) )) {
                RotateAnimation animacion = new RotateAnimation(
                        orientacionAnterior,
                        -(orientacionDispositivo + orientacionDada),
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF,
                        0.5f);
                animacion.setDuration(250);
                animacion.setFillAfter(true);
                brujulaActivity.iniciarAnimacionPuntero(animacion, esOrientacionBuena(orientacionDispositivo));
            }

            orientacionAnterior = -(orientacionDispositivo+orientacionDada);
        }
    }

    /*
     * Usando el mensaje reconocido por voz (será del tipo "norte diez")
     * calculamos la orientación en grados que hace referencia:
     *  - Norte: 0
     *  - Este: 90
     *  - Sur: 180
     *  -
     */
    protected float calcularOrientacionDada(String mensaje){
        if (mensaje.startsWith("norte"))
            return 0;
        else if (mensaje.startsWith("este"))
            return 90;
        else if(mensaje.startsWith("sur"))
            return 180;
        else if (mensaje.startsWith("oeste"))
            return 270;
        else
            return 0;
    }

    /*
     * Usando el mensaje reconocido por voz (será del tipo "norte diez")
     * calculamos el margen de error que hace referencia, esto es,
     * el número que se corresponde a la segunda palabra (en "norte diez", 10)
     */
    protected int calcularMargenError(String mensaje){
        String[] partes = mensaje.split(" ");

        return Integer.parseInt(partes[1]);
    }

    /*
     * Comprobamos si la orientación del dispositivo esta suficientemente cercana
     * a la orientación dada por el usuario al principio de la aplicación.
     * Para ello tenemos en cuenta el margen de error.
     */
    protected boolean esOrientacionBuena(float orientacionDispositivo){
        return orientacionDada - margenError/2 <= orientacionDispositivo &&
                orientacionDispositivo <= orientacionDada + margenError/2;
    }

    // Esta función necesita estar definida aunque no es necesario implementarla en nuestro caso
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
