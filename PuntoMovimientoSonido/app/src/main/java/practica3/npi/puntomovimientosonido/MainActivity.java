package practica3.npi.puntomovimientosonido;

import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private AcelerometroData ad;
    private TextView someTextView1;
    private TextView someTextView2;
    private TextView someTextView3;

    SoundPool ourSounds;
    int soundExplosion;
    Button playExplosion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // "this" allows to make calls back to the BrujulaActivity
        ad = new AcelerometroData((SensorManager) getSystemService(SENSOR_SERVICE), this);
        someTextView1 = (TextView) findViewById(R.id.my_text_view1);
        someTextView2 = (TextView) findViewById(R.id.my_text_view2);
        someTextView3 = (TextView) findViewById(R.id.my_text_view3);


        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        ourSounds = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();
        // TODO buscar sonido libre
        soundExplosion = ourSounds.load(this, R.raw.explosion, 1);
        ourSounds.play (soundExplosion, 0.9f, 0.9f, 1, 0, 1);


        playExplosion = (Button) findViewById(R.id.button);
        playExplosion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ourSounds.play (soundExplosion, 0.9f, 0.9f, 1, 0, 1);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        ad.onResume();
    }

    protected void onPause() {
        super.onPause();
        ad.onPause();
    }

    protected void setTextView1Value(float value1, float value2) {
        someTextView1.setText(Float.toString(value1) + " | " + Float.toString(value2));
    }

    protected void setTextView2Value(float value1, float value2) {
        someTextView2.setText(Float.toString(value1) + " | " + Float.toString(value2));
    }

    protected void setTextView3Value(float value1, float value2) {
        someTextView3.setText(Float.toString(value1) + " | " + Float.toString(value2));
    }

    protected void reproducirSonido(){
        ourSounds.play (soundExplosion, 0.9f, 0.9f, 1, 0, 1);
    }
}
