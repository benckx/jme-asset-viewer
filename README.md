<a href="https://paypal.me/benckx/2">
<img src="https://img.shields.io/badge/Donate-PayPal-green.svg"/>
</a>

# About

3D assets made in Blender don't always appear as expected in jMonkeyEngine games.

I made this tool to help smooth the work between 3D designers and developers, by providing 3D designers a simple GUI to
quickly test assets in a jMonkeyEngine scene and change their settings at runtime.

# How To

## Run

Run the Main file in your favorite IDE. There is no binary runnable yet.

### Arguments

- `skip-jme` doesn't show the JME setting window on start-up
- `no-lighting` doesn't add lights to the scene

## Navigate the Scene

- Move with right-click or WASD keys
- Zoom in and out with mouse wheel
- Rotate with B and N or by pressing Left Control when moving with right-click

The app uses the *ouistiti* camera manager, check README for more information:

https://github.com/benckx/ouistiti

## Build

```
./gradlew clean build
```

## Update dependencies

```
 ./gradlew dependencyUpdates --refresh-dependencies
```

# Known Issues

## File Selection on Linux

```
X Error of failed request:  BadWindow (invalid Window parameter)
  Major opcode of failed request:  20 (X_GetProperty)
  Resource id in failed request:  0x9c0007c
  Serial number of failed request:  2511
  Current serial number in output stream:  2511
```

It doesn't happen when only the GUI is loaded.

## Offset Bounding Box

The green Bounding Box that highlights asset selection in the JME scene shifts out of its center when the asset is
scaled or rotated. I think it has to do with the w rotation parameter, as I don't fully
understand [Quaternion](https://javadoc.jmonkeyengine.org/v3.4.0-stable/index.html) yet

# Related Projects

* **ouistiti**: A basic camera system for management game:<br/>
  https://github.com/benckx/ouistiti
* **chimp-utils**: Collection Kotlin of APIs and Helper:<br/>
  https://github.com/benckx/chimp-utils
