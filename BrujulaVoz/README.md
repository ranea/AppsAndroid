# BrujulaVoz

[gif]

## TO DO

* [x] Spell-check ES para Atom.
* [ ] Eliminar codigo comentado
* [x] Añadir referencia a las imagenes e iconos utilizados (md)
* [ x] Implementar los TO DO y documentar lo que se haya implementado
* [ ] Hacer el tutorial (con snapshot y gif). Esto lo último

## Descripción de la aplicación

La aplicación BrujulaVoz es una aplicación sencilla para Android que hace uso del reconocimiento de voz y de los sensores acelerómetro y magnetómetro.

Primeramente, se le pedirá al usuario que le diga a la aplicación por voz una dirección y un margen de error, por ejemplo:
 - norte diez
 - sur treinta y nueve


[snapshot1 - OKgoogle]

Con estos datos, aparecerá una flecha que actuará de brújula. La flecha se gira a la par que se va girando el dispositivo y cuando la flecha está enderezada, el dispositivo apunta hacia donde dijera el usuario en el primer paso
(norte o sur en los ejemplos).

[snapshot2 - azul]

Además, la flecha se pondrá de color verde cuando el dispositivo esté en la orientación correcta dentro del margen de error. Lo vemos con un ejemplo:
 - Supongamos que el usuario dijo "norte diez".
 - Cuando la orientación del dispositivo esté entre -5 y 5 grados, la flecha se pondrá de color verde.
 - Esto quiere decir que la flecha estará verde cuando esté completamente enderezada (0 grados), levemente hacia la izquierda (hasta -5 grados) o levemente hacia la derecha (hasta +5 grados).


[snapshot3 - verde]

Si por el contrario el usuario hubiera dicho "sur 45", la flecha se pondría verde cuando estuviera enderezada (ahora se da cuando el dispositivo está a 180º), y como el margen de error es elevado, seguiría verde aunque se moviera el dispositivo un poco hacia algún lado.


## Implementación

*Nota*: todo el código esta bastante documentado. La siguiente explicación es una breve descripción a muy alto nivel de como se ha implementado la aplicación.


La primera parte de la aplicación es la correspondiente a la actividad *MainActivity*.

### MainActivity

Para lanzar el reconocimiento de voz, el usuario debe pulsar en la imagen del micrófono que hay en la pantalla. Para ello basta añadir un método *setOnClickListener()* a dicha imagen.

En este método se encuentra *iniciarReconocimientoVoz()*, que es el que se encarga de realizar el reconocimiento de voz. Internamente crea un Intent de tipo *RecognizerIntent.ACTION_RECOGNIZE_SPEECH* que se ejecutará en la misma actividad mediante *startActivityForResult()*.

En *onActivityResult()* manejamos los datos que devuelve el módulo del reconocimiento de voz. Básicamente obtenemos un *String* de los datos devueltos y comprobamos en *validarMensaje()* (mediante expresiones regulares) que este String es de la forma `<dirección> <error>` donde:
 - `<dirección>` debe ser una de las siguientes palabras: norte, sur, este u oeste
 - `<error>` debe ser un número entero.

Si el mensaje se valida, se procede a la siguiente actividad, *BrujulaActivity*. Si no, se le permite al usuario que reintente el proceso.

### BrujulaActivity

Esta actividad recoge la segunda parte de la aplicación. Se encarga de mostrar al usuario una brújula que apunta a la dirección señalada anteriormente por el reconocimiento de voz.

La toma y procesamiento de datos de los sensores se hace en la clase *BrujulaData*. El manejo de sensores en Android es bastante sistemático: se crea un objeto *SensorManager*, se crean objetos asociados a los sensores con *getDefaultSensor()* y se registran *listeners* para recibir datos continuamente de los sensores. En el método *onSensorChanged()* es donde está el procesamiento de los datos.

En dicho método se obtiene la orientación (concretamente el azimut) del dispositivo. Para obtener la orientación, primero se obtiene la matriz de rotación con *getRotationMatrix()* y después se obtiene el azimut con *getOrientation()*. Como se obtiene el azimut en radiantes, se pasa a grados y ya tenemos la orientación del dispositivo (en el sentido de la brújula: 0º marca el norte, 180º el sur...). Utilizando la orientación anterior y la actual se crea una animación de tipo *RotateAnimation* que se aplicará a la imagen de una flecha de tal forma que la flecha se moverá según vayamos moviendo el dispositivo.

*BrujulaActivity* se encarga de mostrarle la animación al usuario, en particular esto se hace en *iniciarAnimacionPuntero()*. Además si *BrujulaData* calculó que la orientación del dispositivo era muy próxima a la orientación dada por el usuario en el reconocimiento de voz, en *iniciarAnimacionPuntero()* se cambia la flecha por defecto azul en una flecha verde, indicando así al usuario la orientación correcta.


## Bibliografía

### Android básico

[Tutorial para empezar en Android](http://developer.android.com/training/basics/firstapp/index.html)

[Componentes fundamentales](http://developer.android.com/intl/es/guide/components/fundamentals.html)

### Reconocimiento de voz

[Parámetros del RecognizerIntent](http://developer.android.com/intl/es/reference/android/speech/RecognizerIntent.html)

[Tutorial sencillo de reconocimiento de voz](http://www.jameselsey.co.uk/blogs/techblog/android-how-to-implement-voice-recognition-a-nice-easy-tutorial/)

[Otro tutorial más complejo](http://www.truiton.com/2014/06/android-speech-recognition-without-dialog-custom-activity/)

[Expresiones regulares](http://www.tutorialspoint.com/java/java_regular_expressions.htm)

### Brújula

[Información general sobre los sensores en Android](http://developer.android.com/intl/es/guide/topics/sensors/sensors_overview.html)

[Tutorial en español de cómo crear una brújula](http://agamboadev.esy.es/como-crear-un-brujula-en-android/)

[Tutorial en inglés de cómo crear una brújula](http://www.techrepublic.com/article/pro-tip-create-your-own-magnetic-compass-using-androids-internal-sensors/)

[Información sobre SensorManager](http://developer.android.com/intl/es/reference/android/hardware/SensorManager.html)

[Idea para el esquema de BrujulaData ](http://stackoverflow.com/questions/15074905/sensor-activity-in-android/18686734#18686734)

[Definición de azimut](https://es.wikipedia.org/wiki/Acimut)

[Parámetros del RotateAnimation](http://developer.android.com/intl/es/reference/android/view/animation/RotateAnimation.html)


## Licencias externas

Las imágenes utilizadas pertenece al conjunto de [Material icons](https://design.google.com/icons/) de Google. Disponen de licencia Creative Common Attribution 4.0 International License.

Para crear los iconos de la aplicación en el launcher, se ha utilizado la herramienta [Launcher Icon Generator](https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html) de Roman Nurik, con licencia Apache.
