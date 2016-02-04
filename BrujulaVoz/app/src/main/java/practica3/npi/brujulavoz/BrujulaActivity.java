package practica3.npi.brujulavoz;

/*
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BrujulaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brujula);
    }
}*/

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class BrujulaActivity extends AppCompatActivity {
    private BrujulaData bd;
    private TextView someTextView;
    private ImageView mPointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brujula);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // "this" allows to make calls back to the BrujulaActivity
        bd = new BrujulaData((SensorManager) getSystemService(SENSOR_SERVICE), this, message);
        someTextView = (TextView) findViewById(R.id.my_text_view);
        mPointer = (ImageView) findViewById(R.id.pointer);

    }

    protected void onResume() {
        super.onResume();
        bd.onResume();
    }

    protected void onPause() {
        super.onPause();
        bd.onPause();
    }

    protected void setTextViewValue(float value) {
        someTextView.setText(Float.toString(value));
    }

    protected void startAnimationPointer(RotateAnimation ra, Boolean correctPosition) {
        if (correctPosition){
            ImageView lineColorCode = (ImageView) findViewById(R.id.pointer);
            int color = Color.parseColor("#008000"); //The color u want
            lineColorCode.setColorFilter(color);
        }
        else{
            ImageView lineColorCode = (ImageView) findViewById(R.id.pointer);
            int color = Color.parseColor("#0000FF"); //The color u want
            lineColorCode.setColorFilter(color);
        }


        mPointer.startAnimation(ra);
    }
}