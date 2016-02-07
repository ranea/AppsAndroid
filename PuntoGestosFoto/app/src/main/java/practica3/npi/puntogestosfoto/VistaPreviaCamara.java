package practica3.npi.puntogestosfoto;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

// Clase básica para la vista previa de la cámara
public class VistaPreviaCamara extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "VistaPreviaCamara";
    private SurfaceHolder surfaceHolder;
    private Camera camara;

    public VistaPreviaCamara(Context context, Camera camera) {
        super(context);
        this.camara = camera;

        // Añadimos una SurfaceHolder.Callback para que nos notifique
        // cuando la surface (que contiene a la vista previa)
        // es creada y destruida
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // La surface se ha creado. Le decimos a la camara que dibuje la vista previa
        // en la surface e iniciamos la vista previa.
        try {
            camara.setPreviewDisplay(holder);
            camara.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error fijando la vista previa: " + e.getMessage());
        }
    }

    // Necesario definirlo pero no implementarlo en nuestro caso
    // (ya se encarga la actividad de liberar la cámara)
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    // Necesario definirlo pero no implementarlo en nuestro caso
    // (no permitimos cambios en la cámara ni en la surface)
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // TODO ELIMINAR EN REV FINAL.
        /*
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (surfaceHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camara.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            camara.setPreviewDisplay(surfaceHolder);
            camara.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camara preview: " + e.getMessage());
        }*/
    }
}