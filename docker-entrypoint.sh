#! /bin/bash

set -e;

cd /app;

# Ensure Android SDK is installed
if [ ! -d /app/android-studio ]; then
  echo "Android SDK directory not found.";

  if [ ! -f /app/android-sdk.zip ]; then
    echo "Android SDK zip file not found; beginning download.";

    # Download the latest version of Android SDK
    curl -L "$( \
      curl -sL "https://developer.android.com/studio/index.html" \
        | grep -Eoh "https?://.*?android-studio-ide.*?-linux\.zip" \
    )" > /app/android-sdk.zip;

  fi

  # Unzips file to /app/android-studio
  unzip /app/android-sdk.zip;
fi

# Sign Android SDK license
if [ ! -d /app/android-studio/licenses/android-sdk-license ]; then
  mkdir -p /app/android-studio/licenses;
  echo -e "\n8933bad161af4178b1185d1a37fbf41ea5269c55" \
    > /app/android-studio/licenses/android-sdk-license;
fi

# Generate self-signed cert
keytool -genkey -noprompt \
  -alias selfsigned \
  -dname "CN=UNKNOWN, OU=UNKNOWN, O=UNKNOWN, L=UNKNOWN, S=UNKNOWN, C=UNKNOWN" \
  -keystore /root/.keystore \
  -storepass password \
  -keypass password;

# Create Lens Launcher APK
/app/gradlew assembleRelease \
  -Pandroid.injected.signing.key.alias=selfsigned \
  -Pandroid.injected.signing.store.file=/root/.keystore \
  -Pandroid.injected.signing.store.password=password \
  -Pandroid.injected.signing.key.password=password;

# Find the generated APK file in lens-launcher/app/build/outputs/apk/app-release.apk
