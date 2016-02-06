package practica3.npi.brujulavoz;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RECONOCIMIENTO_VOZ = 0;
    public final static String EXTRA_MENSAJE = "practica3.npi.brujulavoz.MENSAJE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // El reconocimiento de voz se ejecuta una 1ª vez automáticamente.
        iniciarReconocimientoVoz();
        
        // Si no se reconoce el tipo de mensaje que se busca, se deja
        // al usuario que reintente el reconocimiento con un botón.
        Button btnReintentarReconVoz = (Button) findViewById(R.id.reintentarReconocimientoVoz);
        btnReintentarReconVoz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarReconocimientoVoz();
            }
        });
    }

    /**
     * Lanza el reconocimiento de voz mediante un Intent.
     * LANGUAGE_MODEL_FREE_FORM es necesario para reconocer el habla tal cual
     * (a diferencia de LANGUAGE_MODEL_WEB_SEARCH que está basado en términos
     * de búsqueda en la web).
     */
    protected void iniciarReconocimientoVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // TODO ¿necesario pasarlo al fichero string?
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Di una dirección cardinal y un margen de error");
        startActivityForResult(intent, RECONOCIMIENTO_VOZ);
    }

    /**
     * Maneja los resultados de la Activity del reconocimiento de voz.
     */
    @Override
    protected void onActivityResult(int codigoSolicitud, int codigoResultado, Intent datos) {
        // Comprueba que el intent lanzado sea el del reconocimiento de voz
        if (codigoSolicitud == RECONOCIMIENTO_VOZ && codigoResultado == RESULT_OK) {
            // Los resultados de reconocimiento ordenados descendientemente según la confianza de éxito,
            // esto es, el primero es el más acertado.
            ArrayList<String> resultadosReconocimiento = datos.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String mensaje = resultadosReconocimiento.get(0).toString().toLowerCase();

            // Mostramos en un mensaje efímero y emergente (un Toast) el texto reconocido.
            Toast.makeText(this, "Texto reconocido: " + mensaje, Toast.LENGTH_LONG).show();

            // TODO ELIMINAR EN REV FINAL. En vez de Toast, usar un botón.
            // TextView textView = (TextView) findViewById(R.id.mensaje_reconocido);
            // textView.setText(message);

            // TODO implementar validarMensaje
            // Comprobamos que el mensaje es del tipo que buscamos
            if (validarMensaje(mensaje)){
                // En caso afirmativo, lanzamos la actividad que contiene la brujula.
                // Le mandamos por EXTRA el mensaje que hemos reconocido (para que sepa donde apuntar)
                Intent intent = new Intent(this, BrujulaActivity.class);
                intent.putExtra(EXTRA_MENSAJE, mensaje);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Debe decir algo del tipo: " + "norte 10", Toast.LENGTH_LONG).show();
            }
        }

        super.onActivityResult(codigoSolicitud, codigoResultado, datos);
    }

    /**
     * Comprueba que el contenido del mensaje es de la forma "<direccion> <error>" donde
     *  - direccion debe ser una de las siguientes palabras: norte, sur, este u oeste
     *  - error debe ser un número entero
     */
    protected boolean validarMensaje(String mensaje){
        // TODO implemetar aquí la validación del mensaje, esto es, que sea Norte + número
        // TODO ELIMINAR EN REV FINAL. Forma cutre y no final de implementarlo
        List<String> myList = Arrays.asList("norte", "sur", "este", "oeste");
        return myList.contains(mensaje);
    }
}
