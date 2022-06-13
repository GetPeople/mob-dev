# GetPeople Mobile Development

## Getting Started

### Prerequisite
1. [Android Studio](https://developer.android.com/studio)

2. Java Development Kit (JDK)

3. Android Phone

### Installation  

1. Get an API Key from [Google Maps Platform](https://developers.google.com/maps/documentation/android-sdk/get-api-key)

2. Clone this repository

3. Open the project in Android Studio

4. Enter your API Key in `build.gradle` -> `android` -> `defaultConfig`
    > buildConfigField 'String', 'MAPS_API_KEY', '"your_api_key"'
5. Click `Build` → `Build Bundles(s) / APK(s)` → `Build APK(s)`

6. Wait until you get the notification that it's finished building. Then, click `locate` to locate the .apk file.

7. Move the .apk file to your phone

8. Run the app.
