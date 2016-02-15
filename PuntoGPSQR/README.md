![PuntoGPSQR](app/src/main/res/mipmap-hdpi/ic_launcher.png)

# PuntoGPSQR

[gif]

## TO DO

* [ ] Añadir las snapshot y el gif (para ti, @analca3)
* [ ] Corregir los niveles de markdown

## Descripción de la aplicación

La aplicación PuntoGPSQR es una aplicación sencilla que hace uso de la cárama, reconocimiento de QRs y el GPS.

[snapshot - inicio app]

Primeramente, el usuario deberá echarle una foto a un QR que disponga de una latitud y una longitud de una localización.

[snapshot - fotoQR]

Si el QR dispone de tal información, se procesará automaticámente y se lanzará *Google Navigation* para realización una navegación GPS hacia el punto que indicaba el QR.

[snapshot - googleNavigation]

Además, mientras se muestra indicaciones para relizar el recorrido, se va guardando periódicamente la localización del usuario. Así, cuando el recorrido termina, se muestra en un mapa el recorrido realizado por el usuario.

[snapshot - mapa con el recorrido del usuario]


## Implementación

*Nota*: todo el código esta bastante documentado. La siguiente explicación es una breve descripción a muy alto nivel de como se ha implementado la aplicación.


La primera parte de la aplicación es la correspondiente a la actividad *MainActivity*.

### MainActivity

Para lanzar la toma de la foto, el usuario debe pulsar en la imagen de la cámara que hay en la pantalla. Para ello basta añadir un método *setOnClickListener()* a dicha imagen.

Este método llama a *tomarFoto()*, que mediante el *Intent* *MediaStore.ACTION_IMAGE_CAPTURE* se puede realizar una foto de forma muy sencilla, ya que llama a la aplicación de la cámara del teléfono. En *onActivityResult()*, se almacena la foto tomada en un objeto *Bitmap* que será necesario para el detector de QR.

El detector de QR que hemos usado es el que proporciona la API de *MobileVision* de *Google Play Services* , luego el usuario no tendrá que instalar aplicaciones adicionales. En [Bibliografía](https://github.com/ranea/AppsAndroid/tree/master/PuntoGPSQR#bibliografía) se encuentra de forma detallada como se añade esta librería.

Este detector lo inicializamos en *onCreate()* mediate un objeto *BarcodeDetector*. Así, volviendo a *onActivityResult()*, creamos un *Frame* asociado a la foto y le pasamos el *Frame* al detector QR usando la función *detect()*. Esta función devuelve un *SparseArray<Barcode>* y la primera componente de dicho array es el mensaje obtenido del QR. Obtenido el mensaje, lo validamos usando *validarMensaje()*, esto es, comprobamos que el mensaje embebido en el QR es de la forma `LATITUD_<lat>_LONGITUD_<lng>`. En caso afirmativo iniciamos *NavegacionActivity* mediante un intent para empezar la navegación GPS y la toma de datos mediante el GPS.

### NavegacionActivity

### LocalizacionService


## Bibliografía

### Detección del QR

[Detección de QR con la API de Mobile Vision](https://search-codelabs.appspot.com/codelabs/bar-codes#1)

[Información sobre Mobile Vision APi](https://developers.google.com/vision/introduction)

[SparseIntArray](http://developer.android.com/intl/es/reference/android/util/SparseIntArray.html)


### Cámara

[Uso de la app de la cámara I](http://developer.android.com/intl/es/guide/topics/media/camera.html#intents)

[Uso de la app de la cámara II](http://developer.android.com/intl/es/guide/topics/media/camera.html#intent-receive)


## Licencias externas

Las imágenes utilizadas pertenece al conjunto de [Material icons](https://design.google.com/icons/) de Google. Disponen de licencia Creative Common Attribution 4.0 International License.

Para crear los iconos de la aplicación en el launcher, se ha utilizado la herramienta [Launcher Icon Generator](https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html) de Roman Nurik, con licencia Apache.