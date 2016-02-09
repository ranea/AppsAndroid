# PuntoGestosFoto

[gif]

## TO DO

* [ ] Eliminar código comentado
* [ ] Implementar los TO DO y documentar lo que se haya implementado
* [ ] Hacer el tutorial (con snapshot y gif). Esto lo último
* [ ] Si hay tiempo, cambiar el orden (1ª vez que salte la creación; permitir el cambio de gesto si se ha introducido el anterior)

## Descripción de la aplicación

La aplicación PuntoGestosFoto es una aplicación sencilla para Android que hace uso de reconocimiento de gestos para lanzar una foto automáticamente.

[snapshot1 -  menu]

En primer lugar, el usuario deberá crear un gesto. El gesto es análogo al patrón que usa Android en el bloqueo de pantalla y el procedimiento para crearlo es similar. Para el usuario será fácil pues está ya familiarizado.

[snapshot2 - creación]

Posteriormente, si introduce el gesto que habría creado, se tomará una foto automáticamente a los tres segundos. Si introduce otro gesto no se tomará la foto.

[snapshot3 - introduccion]

[snapshot4 - foto]


## Implementación

*Nota*: todo el código esta bastante documentado. La siguiente explicación es una breve descripción a muy alto nivel de como se ha implementado la aplicación.

La funcionalidad de esta app recae en dos actividades. La actividad *MainActivity* se encarga del manejo y procesamiento del gesto y la actividad *VistaPreviaCamara* se encarga de la toma de la foto y la previsualización de la misma.

Hay que destacar que para el manejo del gesto se ha utilizado la librería [android-lockpattern](https://bitbucket.org/haibison/android-lockpattern) que lo simplifica enormemente. Para poder utilizarla en la aplicación es necesario previamente introducirla en los *build.gradle*. En el apartado [Bibliografía](https://github.com/ranea/AppsAndroid/tree/master/PuntoGestosFoto#bibliografía) se encuentra este procedimiento detallado.

### MainActivity

Y.

### VistaPreviaCamara

X.


## Bibliografía

### Android básico

[Tutorial para empezar en Android](http://developer.android.com/training/basics/firstapp/index.html)

[Componentes fundamentales](http://developer.android.com/intl/es/guide/components/fundamentals.html)

### Android-lockpattern

[Uso de la librería android-lockpattern](https://bitbucket.org/haibison/android-lockpattern/wiki/Quick-Use)

### X

[]()


## Licencias externas

Las imágenes utilizadas pertenece al conjunto de [Material icons](https://design.google.com/icons/) de Google. Disponen de licencia Creative Common Attribution 4.0 International License.

Para crear los iconos de la aplicación en el launcher, se ha utilizado la herramienta [Launcher Icon Generator](https://romannurik.github.io/AndroidAssetStudio/icons-launcher.html) de Roman Nurik, con licencia Apache.
