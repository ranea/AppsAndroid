package practica3.npi.puntogpsqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public final static String EXTRA_MESSAGE = "practica3.npi.puntogpsqr.MENSAJE";
    private Uri fileUri;
    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent2 = new Intent(this, NavegacionActivity.class);
        startActivity(intent2);

/*        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView myImageView = (ImageView) findViewById(R.id.imgview);
                //Bitmap myBitmap = BitmapFactory.decodeResource(
                //        getApplicationContext().getResources(),
                //        R.drawable.puppy);
                myImageView.setImageBitmap(myBitmap);

                TextView txtView = (TextView) findViewById(R.id.txtContent);
                BarcodeDetector detector =
                        new BarcodeDetector.Builder(getApplicationContext())
                                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                                .build();
                if (!detector.isOperational()) {
                    txtView.setText("Could not set up the detector!");
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
                SparseArray<Barcode> barcodes = detector.detect(frame);
                // TODO detectar si se ha leido un barcodeee


                Barcode thisCode = barcodes.valueAt(0);
                txtView.setText(thisCode.rawValue);

                Toast.makeText(MainActivity.this, thisCode.rawValue, Toast.LENGTH_LONG).show();
                // TODO si no son coordenadas validas, REPETIR

                // http://stackoverflow.com/questions/19464100/starting-intent-from-onclicklistener
                Intent intent = new Intent(v.getContext(), NavegacionActivity.class);
                intent.putExtra(EXTRA_MESSAGE, thisCode.rawValue);
                startActivity(intent);
            }
        });*/

    }

/*    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    *//** Create a file Uri for saving an image or video *//*
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    *//** Create a File for saving an image or video *//*
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
//                Toast.makeText(this, "Image saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();
                myBitmap = (Bitmap) data.getExtras().get("data");
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }
}
