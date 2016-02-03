package practica3.npi.brujulavoz;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int RECONOCIMIENTO_VOZ = 0;
    public final static String EXTRA_MESSAGE = "practica3.npi.brujulavoz.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void iniciarReconocimientoVoz(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla");
        startActivityForResult(intent, RECONOCIMIENTO_VOZ);
    }

    /**
     * Handle the results from the recognition activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECONOCIMIENTO_VOZ && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            TextView textView = (TextView) findViewById(R.id.mensaje_reconocido);
            textView.setText(matches.get(0).toString());

            if (matches.get(0).toString().equals("life")) {
                System.out.println("YESSS");
/*                TextView textView2 = new TextView(this);
                textView2.setTextSize(40);
                textView2.setText(matches.get(0).toString());
                LinearLayout layout = (LinearLayout) findViewById(R.id.content);
                layout.addView(textView2);*/

                Intent intent = new Intent(this, BrujulaActivity.class);
                String message = matches.get(0).toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
