# EN1 Goose Game

A desktop game project built with libGDX and Java 21.

## Run

On Windows, from the project root:

```powershell
.\gradlew.bat lwjgl3:run
```

On macOS or Linux:

```sh
./gradlew lwjgl3:run
```

The Gradle wrapper downloads the project’s Gradle version on its first run. The desktop launcher is in the `lwjgl3` module and shared game code is in `core`.

## Build

```powershell
.\gradlew.bat clean build
```

This repository currently builds the desktop application; it does not include a browser or GitHub Pages build.
