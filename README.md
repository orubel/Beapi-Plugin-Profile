# Beapi-Plugin-Profile

To build this profile, simple clone this repo, then go into the root directory and run the following:

```
./gradlew clean;./gradlew install
```

This builds the project in a localized maven repo that you can use on local builds.

Yopu can then build local projects like so:

```
grails create-app <name of your app> --profile beapi
```

This builds all necessary dependencies into your application. The only thing left is to follows direction for [configuration](https://www.beapi.io/installation).
