package practica3.npi.brujulavoz;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    private static final int RECONOCIMIENTO_VOZ = 0;
    public final static String EXTRA_MENSAJE = "practica3.npi.brujulavoz.MENSAJE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Si no se reconoce el tipo de mensaje que se busca, se deja
        // al usuario que reintente el reconocimiento tocando el icono del micrófono.
        ImageView imgMicrofono = (ImageView) findViewById(R.id.microfono);
        imgMicrofono.setOnClickListener(new View.OnClickListener() {
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
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.ayuda_prompt));
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

            String mensaje = resultadosReconocimiento.get(0).toLowerCase();

            // Mostramos en un mensaje efímero y emergente (un Toast) el texto reconocido.
            // Toast.makeText(this, getString(R.string.texto_reconocido) + mensaje, Toast.LENGTH_LONG).show();

            // Comprobamos que el mensaje es del tipo que buscamos
            if (validarMensaje(mensaje)){
                // En caso afirmativo, lanzamos la actividad que contiene la brujula.
                // Le mandamos por EXTRA el mensaje que hemos reconocido (para que sepa donde apuntar)
                Intent intent = new Intent(this, BrujulaActivity.class);
                intent.putExtra(EXTRA_MENSAJE, mensaje);
                startActivity(intent);
            }else{
                Toast.makeText(this, getString(R.string.ejemplo_texto_reconocido) + mensaje, Toast.LENGTH_LONG).show();
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
        // Usamos una expresión regular para validar el mensaje.
        // Para usar una expreg en Java basta con crear un objeto pattern
        // pasandole un String con la expreg y después llamar a matcher() y find()
        String patron = "(norte|sur|este|oeste)\\s\\d+";
        Pattern p = Pattern.compile(patron);
        Matcher m = p.matcher(mensaje);
        return m.matches();
    }
}
