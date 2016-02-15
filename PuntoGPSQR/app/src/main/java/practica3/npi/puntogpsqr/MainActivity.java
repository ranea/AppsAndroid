package practica3.npi.puntogpsqr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    private static final int CODIGO_SOLICITUD_TOMAR_IMAGEN = 1;
    public final static String EXTRA_MENSAJE = "practica3.npi.puntogpsqr.MENSAJE";
    private static final String TAG = MainActivity.class.getSimpleName();
    private BarcodeDetector detectorQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO quitar modo debug en REV FINAL
        // Comentar/descomentar el sig bloque para debug
        Intent intentDebug = new Intent(this, NavegacionActivity.class);
        String msgDebug = "LATITUD_37.19678168548899_LONGITUD_-3.62465459523194 ";
        intentDebug.putExtra(EXTRA_MENSAJE, msgDebug);
        startActivity(intentDebug);

        // Iniciamos el lector de QR
        detectorQR =
                new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                        .build();
        if (!detectorQR.isOperational()) {
            Log.w(TAG, "No se ha podido crear el detector");
            return;
        }

        ImageView iconoCamara = (ImageView) findViewById(R.id.iconoCamara);
        iconoCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto();
            }
        });

    }

    // create Intent to take a picture and return control to the calling application
    // start the image capture Intent
    protected void tomarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CODIGO_SOLICITUD_TOMAR_IMAGEN);
    }

    @Override
    protected void onActivityResult(int codigoSolicitud, int codigoResultado, Intent datos) {
        if (codigoSolicitud == CODIGO_SOLICITUD_TOMAR_IMAGEN) {
            if (codigoResultado == RESULT_OK) {
                // Tomamos la foto
                Bitmap foto = (Bitmap) datos.getExtras().get("data");

                // Le pasamos el detector de QR
                Frame frame = new Frame.Builder().setBitmap(foto).build();
                SparseArray<Barcode> barcodes = detectorQR.detect(frame);

                if (barcodes.size() == 0){
                    Toast.makeText(MainActivity.this, getString(R.string.no_qr_encontrado), Toast.LENGTH_LONG).show();
                }
                else{
                    Barcode codigoQR = barcodes.valueAt(0);
                    Toast.makeText(MainActivity.this, codigoQR.rawValue, Toast.LENGTH_LONG).show();

                    if(validarMensaje(codigoQR.rawValue)){
                        Intent intent = new Intent(this, NavegacionActivity.class);
                        intent.putExtra(EXTRA_MENSAJE, codigoQR.rawValue);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(MainActivity.this, getString(R.string.qr_incorrecto), Toast.LENGTH_LONG).show();
                    }
                }


            } else if (codigoResultado == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    /**
     * Comprueba que el contenido del mensaje es de la forma "LATITUD_<lat>_LONGITUD_<lng>"
     * donde lat y lng son números
     */
    protected boolean validarMensaje(String mensaje){
        // Usamos una expresión regular para validar el mensaje.
        // Para usar una expreg en Java basta con crear un objeto pattern
        // pasandole un String con la expreg y después llamar a matcher() y find()
        // Ejemplo de mensaje: LATITUD_37.19678168548899_LONGITUD_-3.62465459523194
        String patron = "LATITUD_-?\\d+\\.\\d+_LONGITUD_-?\\d+\\.\\d+";
        Pattern p = Pattern.compile(patron);
        Matcher m = p.matcher(mensaje);
        return m.matches();
    }
}
