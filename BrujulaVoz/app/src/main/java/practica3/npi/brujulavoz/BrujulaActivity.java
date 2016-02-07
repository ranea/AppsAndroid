package practica3.npi.brujulavoz;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class BrujulaActivity extends Activity {
    private BrujulaData brujulaData;
    private TextView textoOrientacionDispositivo;
    private ImageView imagenPuntero;
    Drawable puntero_azul;
    Drawable puntero_verde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brujula);

        // Recibimos el mensaje de MainActivity que contiene el mensaje de voz de reconocido
        Intent intent = getIntent();
        String mensaje = intent.getStringExtra(MainActivity.EXTRA_MENSAJE);

        // Instaciamos la clase BrujulaData que se encargará de tomar los datos de los sensores
        brujulaData = new BrujulaData((SensorManager) getSystemService(SENSOR_SERVICE), this, mensaje);

        // Modifica el campo de texto que muestra la orientación que propuso el usuario
        // en el mensaje reconocido por voz.
        // TODO seleccionar solo la 1º palabra
        TextView textoOrientacionDada = (TextView) findViewById(R.id.textoOrientacionDada);
        textoOrientacionDada.setText(mensaje.toUpperCase());

        textoOrientacionDispositivo = (TextView) findViewById(R.id.textoOrientacionDispositivo);
        imagenPuntero = (ImageView) findViewById(R.id.imagenPuntero);

        puntero_azul = ResourcesCompat.getDrawable(getResources(), R.drawable.puntero_azul, null);
        puntero_verde = ResourcesCompat.getDrawable(getResources(), R.drawable.puntero_verde, null);
    }

    protected void onResume() {
        super.onResume();
        brujulaData.onResume();
    }

    protected void onPause() {
        super.onPause();
        // Paramos los sensores cuando la app no está en primer plano
        brujulaData.onPause();
    }

    // Modifica el campo de texto que muestra la orientación (en grados) del dispositivo
    // Se llama desde BrujulaData cada vez que el dispositivo tiene una nueva orientación
    protected void editarTextoOrientacionDispositivo(float value, String msg) {
        //textoOrientacionDispositivo.setText("Orientación: " + Float.toString(value));
        textoOrientacionDispositivo.setText(msg);
    }

    // Aplica una animación al puntero para que se mueva desde la orientación
    // anterior del dispositivo a la actual. Se llama desde BrujulaData
    // que es donde se crea la animacion con los datos de la orientaciones.
    protected void iniciarAnimacionPuntero(RotateAnimation animacion, Boolean orientacionCorrecta) {
        // Si el dispositivo esta orientado según la direccion que propuso el usuario
        // en el mensaje reconocido por voz, tomamos el puntero verde.
        // En otro caso, lo dejamos azul.
        if (orientacionCorrecta){
            imagenPuntero.setImageDrawable(puntero_verde);
            // TODO ELIMINAR EN REV FINAL
            //int color = Color.parseColor("#008000"); //color verde
            //imagenPuntero.setColorFilter(color);
        }
        else{
            imagenPuntero.setImageDrawable(puntero_azul);
            // TODO ELIMINAR EN REV FINAL
            //int color = Color.parseColor("#0000FF"); //color azul
            //imagenPuntero.setColorFilter(color);
        }

        // Le aplicamos la animación al puntero
        imagenPuntero.startAnimation(animacion);
    }
}