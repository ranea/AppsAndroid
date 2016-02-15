![PuntoGPSQR](app/src/main/res/mipmap-hdpi/ic_launcher.png)

# PuntoGPSQR

[gif]

## TO DO

* [ ] Añadir las snapshot y el gif (para ti, @analca3)
* [ ] Corregir los niveles de markdown

## Descripción de la aplicación

La aplicación PuntoGPSQR es una aplicación sencilla que hace uso de la cárama, reconocimiento de QRs y el GPS.

[snapshot - inicio app]

Primeramente, el usuario deberá echarle una foto a un QR que disponga de una latitud y una longitud.

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

El detector de QR que hemos usado es el que proporciona *Google Play Services* , luego el usuario no tendrá que instalar aplicaciones adicionales. En [Bibliografía](https://github.com/ranea/AppsAndroid/tree/master/PuntoGPSQR#bibliografía) se encuentra de forma detallada como se añade esta librería.

Este detector lo inicializamos en *onCreate()* mediate un objeto *BarcodeDetector*. Así, volviendo a *onActivityResult()*, creamos un *Frame* asociado a la foto y le pasamos el *Frame* al detector QR usando la función *detect()*. Esta función devuelve un *SparseArray<Barcode>* y la primera componente de dicho array es el mensaje obtenido del QR. Obtenido el mensaje, lo validamos usando *validarMensaje()*, esto es, comprobamos que el mensaje embebido en el QR es de la forma `LATITUD_<lat>_LONGITUD_<lng>`. En caso afirmativo iniciamos *NavegacionActivity* mediante un intent para iniciar la navegación GPS y la toma de datos mediante el GPS.

### NavegacionActivity

### LocalizacionService


## Bibliografía


### Enlaces 1

http://code.tutsplus.com/tutorials/reading-qr-codes-using-the-mobile-vision-api--cms-24680

https://search-codelabs.appspot.com/codelabs/bar-codes#1

https://developers.google.com/vision/getting-started

https://github.com/googlesamples/android-vision

https://developers.google.com/vision/barcodes-overview

### Enlaces 2

http://developer.android.com/intl/es/guide/topics/media/camera.html#custom-camera

http://developer.android.com/intl/es/guide/topics/media/camera.html#intents

http://developer.android.com/intl/es/guide/topics/media/camera.html#intent-receive

http://stackoverflow.com/questions/16348757/mediastore-extra-output-renders-data-null-other-way-to-save-photos

### Enlaces 3

https://developers.google.com/maps/documentation/android-api/start

https://developers.google.com/maps/documentation/android-api/intents

http://stackoverflow.com/questions/2662531/launching-google-maps-directions-via-an-intent-on-android

http://stackoverflow.com/questions/2662531/launching-google-maps-directions-via-an-intent-on-android

http://stackoverflow.com/questions/13255192/android-google-navigation-intent-modes


# Enlaces 4

http://stackoverflow.com/questions/35395724/send-data-from-service-to-bound-activity-periodically

http://developer.android.com/intl/es/reference/android/util/SparseIntArray.html#valueAt(int)

http://greenrobot.org/eventbus/documentation/how-to-get-started/

https://developers.google.com/android/reference/com/google/android/gms/vision/package-summary

https://developers.google.com/android/reference/com/google/android/gms/vision/barcode/BarcodeDetector#detect(com.google.android.gms.vision.Frame)

http://developer.android.com/intl/es/guide/components/services.html#ExtendingService

http://developer.android.com/intl/es/guide/topics/manifest/activity-element.html#lmode

## Licencias externas
