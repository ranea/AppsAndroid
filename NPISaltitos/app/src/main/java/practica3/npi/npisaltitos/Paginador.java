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

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplificación para {@link android.support.v4.view.ViewPager}
 */
public class Paginador extends FragmentPagerAdapter {

    List<Fragment> listaFragments = null;

    public Paginador(FragmentManager fm) {
        super(fm);
        listaFragments = new ArrayList<Fragment>();
    }

    @Override
    public Fragment getItem(int posicion) {
        return listaFragments.get(posicion);
    }

    @Override
    public int getCount() {
        return listaFragments.size();
    }

    public void addFragment(Fragment fragment) {
        listaFragments.add(fragment);
        notifyDataSetChanged();
    }
}
