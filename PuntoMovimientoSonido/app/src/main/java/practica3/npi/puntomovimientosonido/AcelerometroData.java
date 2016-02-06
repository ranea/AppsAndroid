package practica3.npi.puntomovimientosonido;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class AcelerometroData implements SensorEventListener {

    private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
    private final MainActivity ma;

    private float[] mLastAccelerometer;
    private float[] gravity;
    private float[] linear_acceleration;
    private final float alpha = 0.8f;

//    private float[] mRotationMatrix;
//    private float[] mOrientation;
//    private float mCurrentDegree;

    public AcelerometroData(SensorManager sm, MainActivity ma) {
        // only getSystemService available in context
        mSensorManager = sm;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.ma = ma;

        mLastAccelerometer = null;
        gravity = new float[3];
        linear_acceleration = new float[3];
        /*
        mRotationMatrix = new float[9];
        mOrientation = new float[3];
        mCurrentDegree = 0f;*/
    }

    protected void onResume() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        // Here we call a method in BrujulaActivity and pass it the values from the SensorChanged event
        // ba.setTextViewValue(event.values);

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                mLastAccelerometer = event.values;
                break;
        }

        if ((mLastAccelerometer != null)) {
            // In this example, alpha is calculated as t / (t + dT),
            // where t is the low-pass filter's time-constant and
            // dT is the event delivery rate.

            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            // Remove the gravity contribution with the high-pass filter.
            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            ma.setTextView1Value(gravity[0], linear_acceleration[0]);
            ma.setTextView2Value(gravity[1], linear_acceleration[1]);
            ma.setTextView3Value(gravity[2], linear_acceleration[2]);

            // TODO elegir el gesto (x valor absoluto mayor que tal)

            if (linear_acceleration[0] > 3){
                ma.reproducirSonido();
            }


/*            SensorManager.getRotationMatrix(mRotationMatrix, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);

            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

            ba.setTextViewValue(azimuthInDegress);*/

        }
    }
}