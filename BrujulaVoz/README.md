# BrujulaVoz

## TO DO

* [ ] Spell-check ES para Atom.
* [ ] Eliminar codigo comentado
* [ ] Añadir referencia a las imagenes e iconos utilizados (md)
* [ ] Implementar los TO DO y documentar lo que se haya implementado
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
 - Esto quiere decir que la flecha estára verde cuando esté completamente enderezada (0 grados), levemente hacia la izquierda (hasta -5 grados) o levemente hacia la derecha (hasta +5 grados).


[snapshot3 - verde]

Si por el contrario el usuario hubiera dicho "sur 45", la flecha se pondría verde cuando estuviera enderezada (ahora se da cuando el dispositivo está a 180º), y como el margen de error es elevado, seguiría verde aunque se moviera el dispositivo un poco hacia algún lado.


## Implementación

*Nota*: todo el código esta bastante documento. La siguiente explicación es una breve descripción a muy alto nivel de como se ha implementado la aplicación.


La primera parte de la aplicación es la correspondiente a la actividad *MainActivity*.

### MainActivity

Para lanzar el reconocimiento de voz, el usuario debe pulsar en la imagen del micrófono que hay en la pantalla. Para ello basta añadir un método *setOnClickListener()* a dicha imagen.

En este método se encuentra *iniciarReconocimientoVoz()*, que es el que se encarga de realizar el reconocimiento de voz. Internamente crea un Intent de tipo *RecognizerIntent.ACTION_RECOGNIZE_SPEECH* que se ejecutará en la misma actividad mediante *startActivityForResult()*.

En *onActivityResult()* manejamos los datos que devuelve el módulo del reconocimiento de voz. Básicamente obtenemos un *String* de los datos devueltos y comprobamos en *validarMensaje()* que este String es de la forma `<dirección> <error>` donde:
 - direcciÓn debe ser una de las siguientes palabras: norte, sur, este u oeste
 - error debe ser un número entero.

Si el mensaje se valida, se procede a la siguiente actividad, *BrujulaActivity*. Si no, se le permite al usuario que reintente el proceso.

### BrujulaActivity

...

## Problemas encontrados


## Referencias

## Bibliografía
