package practica3.npi.puntogpsqr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

public class NavegacionActivity extends AppCompatActivity {
    // TODO change name
    private static final int INTENT_V1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegacion);

        LocationTracker tracker = new LocationTracker(getApplicationContext()) {
            @Override
            public void onTimeout() {
                Toast.makeText(getApplicationContext(), "No position found", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLocationFound(Location location) {
                Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();
            }
        };
        tracker.startListening();

        // *********************************************************************************

        //Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // *********************************************************************************
        // Create a Uri from an intent string. Use the result to create an Intent.
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        // Make the Intent explicit by setting the Google Maps package
        //Uri gmmIntentUri = Uri.parse("google.navigation:q=37.19678168548899,-3.62465459523194 &mode=w");
        Uri gmmIntentUri = Uri.parse("google.navigation:q=37.1809053,-3.6133 &mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        startActivityForResult(mapIntent, INTENT_V1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_V1) {
            Toast.makeText(this, "NavegacionActivity: " + getDateTime(), Toast.LENGTH_LONG).show();
        }
    }

    private String getDateTime() {
        // get date time in custom format
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
        return sdf.format(new Date());
    }
}
