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

package practica3.npi.npisaltitos.fragments;

import practica3.npi.npisaltitos.R;
import practica3.npi.npisaltitos.UtilidadesPreferencias;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Fragment que muestra la cuenta de saltos
 */
public class FragmentContador extends Fragment {

    private TextView contador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contador_layout, container, false);
        contador = (TextView) view.findViewById(R.id.counter);
        setCounter(UtilidadesPreferencias.getCounterFromPreference(getActivity()));
        return view;
    }
    public void setCounter(String text) {
        contador.setText(text);
    }

    public void setCounter(int i) {
        setCounter(i < 0 ? "0" : String.valueOf(i));
    }

}
