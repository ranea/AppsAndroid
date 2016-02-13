# NPISaltitos

[gif]

## TO DO

* [ ] Añadir las snapshot y el gif.
* [ ] Revisar licencia JumpingJack

## Descripción de la aplicación

La aplicación NPISaltitos es una aplicación sencilla para Android que hace uso del sensor de gravedad de un dispositivo Android Wear.

El usuario sólo debe colocarse el reloj en la muñeca y saltar uniendo los brazos encima de la cabeza.
Este ejercicio se conoce como *jumping jack*.

[alguna imagen o gif]

Por cada movimiento completo se va incrementando un contador en la pantalla del reloj. Desplazándote hacia la derecha se llega a otra pantalla
en la que se puede resetear el contador.

[snapshots donde se vean los fragments]


## Implementación

*Nota*: todo el código esta bastante documentado. La siguiente explicación es una breve descripción a muy alto nivel de como se ha implementado la aplicación.


La primera parte de la aplicación es la correspondiente a la actividad *MainActivity*.

### MainActivity

En esta actividad se crean las vistas (los fragments) de la aplicación y se va llevando la cuenta de los saltos.
Como necesitamos los sensores, esta actividad implementa la interfaz *SensorEventListener*.

Se crean los fragments en el método *crearVistas()*, el cual crea un *Paginador* y le añade dos fragments: el contador y el que reinicia.

Luego están las funciones de manejo de los saltos. Al cambiar los valores del sensor de gravedad se llama a la función *detectarSalto(float valorX, long tiempo)*, la cual llama a *saltoDetectado(boolean arriba)* si se dan las condiciones de salto: que el cambio de gravedad sea mayor que nuestro umbral (que es 7.0 para dar un margen, siendo 9.8 una mejor aproximación al valor real), que no haya pasado más de un tiempo determinado y que el valor detectado sea positivo (para contar sólo un movimiento de los brazos, en nuestro caso al bajarlos). Al detectar un salto se actualiza el contador. Cuando el número de saltos es múltiplo de 10, el dispositivo vibra.

### FragmentContador

Este fragment es simple: muestra un contador en la pantalla.

## FragmentOpciones

Este fragment sólo muestra un botón grande para reiniciar el contador a 0.

## Paginador

Esta clase se usa para organizar los fragments usando un FragmentPagerAdapter. Se usa esta estructura por dificultad para usar la más actualizada del SDK de Android.

## Bibliografía


### Enlaces

[Sensors Motion](http://developer.android.com/intl/es/guide/topics/sensors/sensors_motion.html)

[PreferenceManager](http://developer.android.com/intl/es/reference/android/preference/PreferenceManager.html)

[SharedPreferences](http://developer.android.com/intl/es/reference/android/content/SharedPreferences.html)

[Vibrator](http://developer.android.com/intl/es/reference/android/os/Vibrator.html)


Idea y estructura de paginador
[JumpingJack](https://github.com/googlesamples/android-JumpingJack)

## Licencias externas

El ejemplo JumpingJack tiene licencia [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)
