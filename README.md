Overview
--------
Android demo using the Google Geocoding, Elevation and Civic Information APIs: the project builds a simple Android application that gives geocoding and elevation data, plus a complete list of elected officials (in the US at least), for a given address string. The application will run fine on a connected device or AVD emulator (use an Android-21 system image). The same source code project can be used to build Maven and ant (Eclipse ADT) builds.  

Using Google APIs
-----------------
You will need a Google API key to access the [Geocoding API](https://developers.google.com/maps/documentation/geocoding/), [Elevation API](https://developers.google.com/maps/documentation/elevation/) and [Google Civic Information API](https://developers.google.com/civic-information/): these services have courtesy limits, which allow limited use free of charge. Use the [Google API Console](https://code.google.com/apis/console) to create a Google API project, add the required services (APIs & Auth -> API) and then create an API Key (APIs & Auth -> Credentials). Create a Browser key and leaved the Allowed Referers field blank.

Create the following class file (`GooglePlacesKey.java`) in `src/com/example/appdynamics/placesdemo` and paste in your API key:
```
package com.example.appdynamics.placesdemo;

public class GooglePlacesKey {
    public static final String API_KEY = "<your API key>";
}

Gradle Builds
-------------
Import the project into Android Studio (tested with beta 0.9.1).  

To build from the commandline: `gradle clean build`
To install: `gradle installDebug`
