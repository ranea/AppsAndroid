package practica3.npi.puntogestosfoto;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamaraActivity extends Activity {
    // String para marcar entradas en en log
    private static final String TAG = "CamaraActivity";

    // Variables relacionadas con la cámara
    private Camera camara;
    private VistaPreviaCamara vistaPreviaCamara;
    private Camera.PictureCallback fotoCallBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Creamos una instancia de Camera
        camara = Camera.open();

        // Introducimos la vista previa de la cámara en el FrameLayout vistaPreviaCamara
        vistaPreviaCamara = new VistaPreviaCamara(this, camara);
        FrameLayout vistaPreviaCamara = (FrameLayout) findViewById(R.id.vistaPreviaCamara);
        vistaPreviaCamara.addView(this.vistaPreviaCamara);

        // Ejecutamos el codigo de tomar una foto a los 3 segundos (3000 milisegundos)
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Tomamos la foto y ejecutamos fotoCallBack()
                camara.takePicture(null, null, fotoCallBack);

                // Mostramos al usuario donde se ha guardado la foto
                String path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES) + getString(R.string.app_name);
                Toast.makeText(CamaraActivity.this, getString(R.string.imagen_guardada) + path, Toast.LENGTH_LONG).show();
            }
        }, 3000);

        // Función que se ejecutará al tomar una foto
        fotoCallBack = new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                // Creamos el archivo (un Uri) que alojará la imagen
                File pictureFile = obtenerArchivoMultimediaDeSalida(getString(R.string.app_name));

                // Alojamos la imagen en dicho archivo
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(data);
                    fos.close();
                } catch (FileNotFoundException e) {
                    Log.d(TAG, "Archivo no encontrado: " + e.getMessage());
                } catch (IOException e) {
                    Log.d(TAG, "Error accediendo al archivo: " + e.getMessage());
                }

                // Volvemos a la actividad principal al echar la foto
                Intent intent = new Intent(CamaraActivity.this, MainActivity.class);
                startActivity(intent);

            }
        };
    }

    // Inhabilita el botón atrás
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        liberarCamara();
    }

    private void liberarCamara(){
        // Paramos la cámara cuando no esté la app en primer plano
        if (camara != null){
            camara.release();
            camara = null;
        }
    }

    // **************************************************************************************
    // *********** El siguiente código es necesario para alojar fotos en la SD **************
    // **************************************************************************************

    // Creamos un archivo Uri para guardar la imagen
    private static Uri obtenerUriDeArchivoMultimediaDeSalida(String nombreCarpeta){
        return Uri.fromFile(obtenerArchivoMultimediaDeSalida(nombreCarpeta));
    }

    // Creamos un archivo para guardar la imagen
    private static File obtenerArchivoMultimediaDeSalida(String nombreCarpeta){
        // Fijamos el siguiente directorio de la SD para almacenar las fotos: "Pictures/PuntoGestosFoto"
        File carpetaFotos = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), nombreCarpeta);

        // Creamos el directorio anterior si no existe
        if (! carpetaFotos.exists()){
            carpetaFotos.mkdirs();
        }

        // Creamos el nombre del archivo multimedia
        String selloDeTiempo = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File archivoMultimedia;
        archivoMultimedia = new File(carpetaFotos.getPath() + File.separator +
                    "IMG_"+ selloDeTiempo + ".jpg");

        return archivoMultimedia;
    }
}
