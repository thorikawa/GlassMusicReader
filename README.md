Music Reader for glass
===========

Music Reader for glass is a google glass application for music performer. You can search any music lyrics/scores by voice command, and turn pages by shaking your head, which means you can sing it with your guitar/piano totally hands-free!

## Demo
You can watch a demo video here: http://www.youtube.com/watch?v=fXXalGkkX7M
[![demo image](http://img.youtube.com/vi/fXXalGkkX7M/hqdefault.jpg)](http://www.youtube.com/watch?v=fXXalGkkX7M)

## Usage
Say "Sing a song" after "Ok Glass". After prompt shows up, say your favorite song name and/or artist name. 

## Download
You can download apk file directry here:
https://github.com/thorikawa/GlassMusicReader/releases/download/v1.0/GlassMusicReader-1.0.apk

## Building

First, you need to obtain [Guitarparty API](http://www.guitarparty.com/developers/api-docs/getting-started/) key to build the app. Please obtain API key and put it into `com.polysfactory.glassmusicreader.C` class.

Also, the build requires [Glass Development Kit (GDK)](https://developers.google.com/glass/develop/gdk/)
to be installed in your development environment. In addition you'll need to set
the `ANDROID_HOME` environment variable to the location of your SDK:

```bash
export ANDROID_HOME=/your/path/to/android-sdk
```

After satisfying those requirements, the build is pretty simple:

* Run `./gradlew assembleDebug` from the root directory build the APK only
* Run `./gradlew installDebug` from the root directory to build and run
  the app, this requires a connected Android device or running
  emulator

You can also use [Android Studio](http://developer.android.com/sdk/installing/studio.html) to build the app.

## Acknowledgements

This project uses the [Guitarparty API](http://www.guitarparty.com/developers/api-docs/getting-started/).

It also uses the following open source libraries:

* [Head Gesture Detector for Google Glass](https://github.com/thorikawa/glass-head-gesture-detector)
* [Guitarparty Java Client Library](https://github.com/thorikawa/guitarparty-java)

## License
```
Copyright 2013 Poly's Factory

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```




