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

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.PreferenceManager;

/**
 * Utilidades para abstraer métodos para guardar preferencias.
 */
public class UtilidadesPreferencias {

    private static final String CLAVE_PREF_CONTADOR = "contador";

    /**
     * Guardar el contador en las preferencias. Si <code>valor</code>
     * es negativo, se borra el valor de las preferencias.
     */
    public static void saveCounterToPreference(Context context, int valor) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        if (valor < 0) {
            // Borramos las preferencias
            pref.edit().remove(CLAVE_PREF_CONTADOR).apply();
        } else {
            pref.edit().putInt(CLAVE_PREF_CONTADOR, valor).apply();
        }
    }

    /**
     * Toma el valor del gestor de preferencias. Si no existe, devuelve
     * <code>0</code>.
     */
    public static int getCounterFromPreference(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(CLAVE_PREF_CONTADOR, 0);
    }

}
