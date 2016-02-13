package practica3.npi.puntogestosfoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

// Librería que se encarga de manejar el gesto (similar al patrón de bloqueo de Android)
import haibison.android.lockpattern.LockPatternActivity;
import haibison.android.lockpattern.utils.AlpSettings;

public class MainActivity extends Activity {
    private static final int SOLICITUD_CREAR_GESTO = 1;
    private static final int SOLICITUD_INTRODUCIR_GESTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Opción para que la librería se encargue de guardar el ultimo patrón creado
        AlpSettings.Security.setAutoSavePattern(this, true);

        // Al tocar el botón, se abre un diálogo para crear el gesto
        Button botonCrearGesto = (Button) findViewById(R.id.botonCrearGesto);
        botonCrearGesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos un Intent que se encarga de lanzar
                // una ventana para que el usuario cree su gesto
                LockPatternActivity.IntentBuilder
                        .newPatternCreator(MainActivity.this)
                        .startForResult(MainActivity.this, SOLICITUD_CREAR_GESTO);
            }
        });

        // Al tocar el botón, se abre un diálogo para crear el gesto
        Button botonIntroducirGesto = (Button) findViewById(R.id.botonIntroducirGesto);
        botonIntroducirGesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos un Intent que se encarga de lanzar una ventana
                // para que el usuario introduzca el gesto anteriormente creado
                LockPatternActivity.IntentBuilder
                        .newPatternComparator(MainActivity.this)
                        .startForResult(MainActivity.this, SOLICITUD_INTRODUCIR_GESTO);
            }
        });
    }

    // Inhabilita el botón atras
    @Override
    public void onBackPressed() {
    }

    /**
     * Maneja los resultados de la creación/introducción del gesto
     */
    @Override
    protected void onActivityResult(int codigoSolicitud, int codigoResultado, Intent datos) {
        // Comprobamos si se lanzó la creación de gesto o la introducción.
        switch (codigoSolicitud) {
            case SOLICITUD_CREAR_GESTO: {
                // No hace falta hacer nada que ya se ha activado el AutoSavePattern
                // y el gesto se guarda automáticamente
                break;
            }
            case SOLICITUD_INTRODUCIR_GESTO: {
                // hay 4 códigos de resultado que debemos de manejar
                switch (codigoResultado) {
                    case RESULT_OK:
                        // El usuario introdujo el gesto correcto
                        Intent intent = new Intent(this, CamaraActivity.class);
                        startActivity(intent);
                        break;
                    case RESULT_CANCELED:
                        // El usuario canceló la tarea
                        break;
                    case LockPatternActivity.RESULT_FAILED:
                        // El usuario introdujo un gesto incorrecto
                        break;
                }

                break;
            }
        }
    }
}
