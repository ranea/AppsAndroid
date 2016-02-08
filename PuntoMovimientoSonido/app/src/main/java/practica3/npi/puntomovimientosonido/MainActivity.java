package practica3.npi.puntomovimientosonido;

import android.app.Activity;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private AcelerometroData acelerometro;
    private TextView textoAceleracion;
    private ImageView imagenIcono;
    private TranslateAnimation animation;

    SoundPool sonidos;
    int sonidoMarioMoneda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instaciamos la clase AcelerometroData que se encargará de tomar el dato del sensor
        acelerometro = new AcelerometroData((SensorManager) getSystemService(SENSOR_SERVICE), this);

        textoAceleracion = (TextView) findViewById(R.id.textoAceleracion);
        imagenIcono = (ImageView) findViewById(R.id.imagenIcono);

        // Creamos una pequeña animación que mueve el icono del móvil a izquierda y a derecha
        // Se ejecutará cuando se agite el móvil (a la vez que se escucha el sonido)
        animation = new TranslateAnimation(-10.0f, 10.0f,
                0.0f, 0.0f);
        animation.setDuration(100);
        animation.setRepeatCount(1);
        animation.setFillAfter(true);

        // Iniciamos el SoundPool que manejará el sonido
        // Como solo vamos a reproducir un sonido, con un canal (Stream) basta
        // El sonido se reproducirá cuando se agite el móvil
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        sonidos = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
        sonidoMarioMoneda = sonidos.load(this, R.raw.coin, 1);
    }

    protected void onResume() {
        super.onResume();
        acelerometro.onResume();
    }

    protected void onPause() {
        super.onPause();
        acelerometro.onPause();
    }

    // Modifica el TextView que contiene la aceleración lineal en el eje X
    // que posee el dispositivo en el momento. Se llama desde AcelerometroData
    protected void fijarTextoAceleracion(float aceleracion) {
        textoAceleracion.setText("Aceleración: " + Float.toString(aceleracion));
    }

    // Reproduce el sonido y realiza la animación de agitar el icono del móvil
    // Se llama desde AcelerometroData cuando la aceleración lineal
    // en el eje X supera un mínimo.
    protected void reproducirSonidoYAnimacion(){
        sonidos.play(sonidoMarioMoneda, 0.9f, 0.9f, 1, 0, 1);
        imagenIcono.startAnimation(animation);
    }
}
