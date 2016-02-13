package practica3.npi.puntogpsqr;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hhrr on 2/10/16.
 */
public class TimeService extends Service {
    // constant
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // display toast
                    Toast.makeText(getApplicationContext(), getDateTime(),
                            Toast.LENGTH_SHORT).show();
                }

            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

    }
}