package practica3.npi.puntogestosfoto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import haibison.android.lockpattern.LockPatternActivity;
import haibison.android.lockpattern.utils.AlpSettings;

public class MainActivity extends AppCompatActivity {
    private static final int REQ_CREATE_PATTERN = 1;
    private static final int REQ_ENTER_PATTERN = 2;
    //private char[] pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlpSettings.Security.setAutoSavePattern(this, true);

        Button btnCrearPatron = (Button) findViewById(R.id.crearPatron);
        btnCrearPatron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockPatternActivity.IntentBuilder
                        .newPatternCreator(MainActivity.this)
                        .startForResult(MainActivity.this, REQ_CREATE_PATTERN);
            }
        });

        Button btnIntroducirPatron = (Button) findViewById(R.id.introducirPatron);
        btnIntroducirPatron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LockPatternActivity.IntentBuilder
                        .newPatternComparator(MainActivity.this)
                        .startForResult(MainActivity.this, REQ_ENTER_PATTERN);
            }
        });

//        Button btnEliminarPatrones = (Button) findViewById(R.id.eliminarPatrones);
//        btnEliminarPatrones.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences preferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.clear();
//                editor.apply();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_CREATE_PATTERN: {
                if (resultCode == RESULT_OK) {
                    char[] pattern = data.getCharArrayExtra(LockPatternActivity.EXTRA_PATTERN);
                    Toast.makeText(this, "Pattern:\n" + pattern, Toast.LENGTH_LONG).show();
                }

                break;
            }// REQ_CREATE_PATTERN
            case REQ_ENTER_PATTERN: {
                // NOTE that there are 4 possible result codes!!!
                switch (resultCode) {
                    case RESULT_OK:
                        // The user passed
                        Toast.makeText(this, "RESULT_OK", Toast.LENGTH_LONG).show();
                        break;
                    case RESULT_CANCELED:
                        // The user cancelled the task
                        Toast.makeText(this, "RESULT_CANCELED", Toast.LENGTH_LONG).show();
                        break;
                    case LockPatternActivity.RESULT_FAILED:
                        // The user failed to enter the pattern
                        Toast.makeText(this, "RESULT_FAILED", Toast.LENGTH_LONG).show();
                        break;
                    case LockPatternActivity.RESULT_FORGOT_PATTERN:
                        // The user forgot the pattern and invoked your recovery Activity.
                        Toast.makeText(this, "RESULT_FORGOT_PATTERN", Toast.LENGTH_LONG).show();
                        break;
                }

                // In any case, there's always a key EXTRA_RETRY_COUNT, which holds the number
                // of tries that the user did.
                int retryCount = data.getIntExtra(LockPatternActivity.EXTRA_RETRY_COUNT, 0);

                break;
            }// REQ_ENTER_PATTERN
        }
    }
}
