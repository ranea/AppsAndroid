/*
 * Copyright (C) 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package practica3.npi.npisaltitos;

import practica3.npi.npisaltitos.fragments.FragmentContador;
import practica3.npi.npisaltitos.fragments.FragmentOpciones;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Actividad principal que recibe información de sensores.
 *
 * Como las aplicaciones wear tienen un tiempo de vida muy corto, modificamos el flag FLAG_KEEP_SCREEN_ON
 * para que al usuario le dé tiempo a realizar acciones, teniendo una variable TIMEOUT_PANTALLA_ENCENDIDA
 * que determina el tiempo máximo de vida si no hay acciones.
 *
 * Se incluye un paginador con dos fragments, uno para ver el número de saltos y otro para resetear
 * el contador. Este valor se mantiene entre ejecuciones de la aplicación.
 */
public class MainActivity extends Activity
        implements SensorEventListener {

    /** Timeout para mantener encendida la pantalla sin acciones **/
    // TODO reducir timeout
    private static final long TIMEOUT_PANTALLA_ENCENDIDA = 200000; // in milliseconds

    /** Umbral que determina el tiempo máximo de un movimiento **/
    private static final long UMBRAL_MOVIMIENTO = 2000000000; // in nanoseconds (= 2sec)

    /**
     * La gravedad es aproximadamente 9.8 m/s^2 pero el usuario podría no completar el movimiento,
     * así que dejamos un margen. Contamos un movimiento válido si el sensor tiene un cambio mayor a
     * UMBRAL_GRAVEDAD.
     */
    private static final float UMBRAL_GRAVEDAD = 7.0f;

    private SensorManager gestorSensores;
    private Sensor sensor;
    private long tiempoAnterior = 0;
    private boolean arriba = false;
    private int contadorSaltos = 0;
    private ViewPager paginador;
    private FragmentContador paginaContador;
    private FragmentOpciones paginaOpciones;
    private ImageView indicadorSegundo;
    private ImageView indicadorPrimero;
    private Timer timer;
    private TimerTask tareaTimer;
    private Handler handler;
    private Vibrator vibrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saltitos_layout);
        crearVistas();
        handler = new Handler();
        vibrador = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        contadorSaltos = UtilidadesPreferencias.getCounterFromPreference(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        reiniciarTimer();
        gestorSensores = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = gestorSensores.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }

    /**
     * Creamos las vistas al crear la actividad. Creamos un paginador y creamos y añadimos los fragments
     * que hemos creado.
     */
    private void crearVistas() {
        paginador = (ViewPager) findViewById(R.id.pager);
        indicadorPrimero = (ImageView) findViewById(R.id.indicator_0);
        indicadorSegundo = (ImageView) findViewById(R.id.indicator_1);
        final Paginador pag = new Paginador(getFragmentManager());
        paginaContador = new FragmentContador();
        paginaOpciones = new FragmentOpciones();
        pag.addFragment(paginaContador);
        pag.addFragment(paginaOpciones);
        setIndicador(0);
        this.paginador.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                setIndicador(i);
                reiniciarTimer();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        this.paginador.setAdapter(pag);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gestorSensores.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gestorSensores.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Le pasamos el primer elemento, que es la variación en X, y el tiempo transcurrido.
        detectarSalto(event.values[0], event.timestamp);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Para detectar el salto, asumimos que la componente X del sensor de gravedad es +9.8
     * cuando la mano está abajo, y -9.8 cuando está arriba (lo cual se invierte si se lleva el reloj
     * en la mano derecha). Usaremos UMBRAL_GRAVEDAD para no ser estrictos con la medida.
     * Se toma el movimiento si tarda menos que UMBRAL_MOVIMIENTO.
     */
    private void detectarSalto(float valorX, long tiempo) {
        if ((Math.abs(valorX) > UMBRAL_GRAVEDAD)) {
            if(tiempo - tiempoAnterior < UMBRAL_MOVIMIENTO && arriba != (valorX > 0)) {
                saltoDetectado(!arriba);
            }
            arriba = valorX > 0;
            tiempoAnterior = tiempo;
        }
    }

    /**
     * Se llama cuando se detecta un salto.
     */
    private void saltoDetectado(boolean up) {
        // Sólo se cuenta cuando hacemos el movimiento completo.
        if (up) {
            return;
        }
        contadorSaltos++;
        setContador(contadorSaltos);
        reiniciarTimer();
    }

    /**
     * Actualiza el contador en la interfaz gráfica. Además lo guarda en las preferencias
     * y hace vibrar el wear cuando llevamos un múltiplo de 10 saltos.
     */
    private void setContador(int i) {
        paginaContador.setCounter(i);
        UtilidadesPreferencias.saveCounterToPreference(this, i);
        if (i > 0 && i % 10 == 0) {
            vibrador.vibrate(200);
        }
    }

    /**
     * Resetea el contador de saltos
     */
    public void resetContador() {
        contadorSaltos = 0;
        setContador(0);
        reiniciarTimer();
    }

    /**
     * Inicia un timer para activar la flag FLAG_KEEP_SCREEN_ON.
     */
    private void reiniciarTimer() {
        if (null != timer) {
            timer.cancel();
        }
        tareaTimer = new TimerTask() {
            @Override
            public void run() {
                resetFlag();
            }
        };
        timer = new Timer();
        timer.schedule(tareaTimer, TIMEOUT_PANTALLA_ENCENDIDA);
    }

    /**
     * Resetea el flag FLAG_KEEP_SCREEN_ON para permitir llevar a segundo plano la aplicación.
     */
    private void resetFlag() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                finish();
            }
        });
    }

    /**
     * Asigna el indicador de fragment activo
     */
    private void setIndicador(int i) {
        switch (i) {
            case 0:
                indicadorPrimero.setImageResource(R.drawable.full_10);
                indicadorSegundo.setImageResource(R.drawable.empty_10);
                break;
            case 1:
                indicadorPrimero.setImageResource(R.drawable.empty_10);
                indicadorSegundo.setImageResource(R.drawable.full_10);
                break;
        }
    }


}
