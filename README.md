# Lens Launcher

Lens Launcher is a unique, efficient way to browse and launch your apps.

![Alt text](resources/art/lens_launcher_demo.gif?raw=true "Title")

Instead of scrolling long lists or swiping through multiple pages, Lens Launcher implements two unique features:

<b>• An Equispaced Grid that displays all of your apps, regardless of screen size or app count.</b>

<b>• A Graphical Fisheye Lens to quickly zoom, pan and launch apps using touch gestures.</b>

Lens Launcher also includes a full Settings menu to tweak all aspects of the Lens; Distortion, Scaling, Icon Size, Haptic Feedback, etc.

The Graphical Fisheye Lens algorithm is derived from methods proposed by Manojit Sarkar and Marc H. Brown. Their original 1993 paper, titled <i>Graphical Fisheye Views</i>, can be found here:
https://drive.google.com/open?id=0B6Nqy9y098RLa2pqNVFUcUhIZXM

Major contributions to Lens Launcher have been made by Rish Bhardwaj (@CreaRo)

You can download Lens Launcher on Google Play:
https://play.google.com/store/apps/details?id=nickrout.lenslauncher

## Build from Source

If you'd like to build from source, you may do so easily with [Docker][1], but
if you are wanting to contribute it may be better to install the build tools on
your machine.

### Easy Way

Building the APK is extremely easy using [`docker-compose`][2], all that is
needed is to run the following inside the repo's directory:

```bash
docker-compose run --rm gradle
```

Then download the APK inside
`lens-launcher/app/build/outputs/apk/app-release.apk` to your phone to install.

### For Development

1.  Install the latest Android SDK from [Android Studio][3] and accept the
    licenses.
2.  Install Java JDK 8 from [Oracle][4].
3.  Run `keytool` and create a keystore following the command prompts.
4.  Generate an APK with `gradlew` like below:

    ```bash
    gradlew assembleRelease \
      -Pandroid.injected.signing.key.alias=$KEY_ALIAS \
      -Pandroid.injected.signing.store.file=$KEY_STORE_PATH \
      -Pandroid.injected.signing.store.password=$KEY_STORE_PASSWORD \
      -Pandroid.injected.signing.key.password=$KEY_PASSWORD;
    ```

[1]: https://docs.docker.com/
[2]: https://docs.docker.com/compose/install/
[3]: https://developer.android.com/studio/index.html
[4]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
