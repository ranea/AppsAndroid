package practica3.npi.brujulavoz;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class BrujulaData implements SensorEventListener {

    private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
    private final Sensor mMagnetometer;
    private final BrujulaActivity ba;
    private final String message;

    private float[] mLastAccelerometer;
    private float[] mLastMagnetometer;
    private float[] mRotationMatrix;
    private float[] mOrientation;
    private float mCurrentDegree;

    public BrujulaData(SensorManager sm, BrujulaActivity ba, String message) {
        // only getSystemService available in context
        mSensorManager = sm;
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        this.ba = ba;
        this.message = message;

        mLastAccelerometer = null;
        mLastMagnetometer = null;
        mRotationMatrix = new float[9];
        mOrientation = new float[3];
        mCurrentDegree = 0f;
    }

    protected void onResume() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
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
            case Sensor.TYPE_MAGNETIC_FIELD:
                mLastMagnetometer = event.values;
                break;
        }

        if ((mLastAccelerometer != null) && (mLastMagnetometer != null)) {
            SensorManager.getRotationMatrix(mRotationMatrix, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mRotationMatrix, mOrientation);

            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;

            ba.setTextViewValue(azimuthInDegress);

            // TODO usar el numero de precision

            Boolean correctPosition = false;
            if (message.equals("norte")) {
                correctPosition = (0 <= azimuthInDegress && azimuthInDegress <= 5) || (355 <= azimuthInDegress && azimuthInDegress <= 360);
            }
            else if (message.equals("sur")) {
                correctPosition = (175 <= azimuthInDegress && azimuthInDegress <= 185);
                azimuthInDegress = (azimuthInDegress + 180)%360;
            }
            else if (message.equals("este")) {
                correctPosition = (85 <= azimuthInDegress && azimuthInDegress <= 95);
                azimuthInDegress = (azimuthInDegress - 90)%360;
            }
            else if (message.equals("oeste")) {
                correctPosition = (265 <= azimuthInDegress && azimuthInDegress <= 275);
                azimuthInDegress = (azimuthInDegress + 90)%360;
            }

            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            ra.setDuration(250);
            ra.setFillAfter(true);

            ba.startAnimationPointer(ra, correctPosition);
            mCurrentDegree = -azimuthInDegress;
        }
    }
}