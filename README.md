<a href="https://paypal.me/benckx/2">
<img src="https://img.shields.io/badge/Donate-PayPal-green.svg"/>
</a>


⚠️ Still in development!

# About

3D assets made in Blender don't always appear as expected in my jMonkeyEngine game.

Therefore, I made this tool to help smooth work between 3D designers and developers, by giving 3D designers a tool to
quickly add some assets to a jMonkeyEngine scene.

# How To

## Run

Run the Main file in your favorite IDE. There is no binary runnable yet.

### Arguments

- `skip-jme` doesn't show the JME setting window on start-up

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
