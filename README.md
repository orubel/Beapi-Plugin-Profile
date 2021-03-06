# Beapi-Plugin-Profile

To build this profile, simple clone this repo, then go into the root directory and run the following:

```
./gradlew clean;./gradlew install
```

This builds the project in a localized maven repo that you can use on local builds.

Yopu can then build local projects like so:

```
grails create-app <name of your app> --profile beapi-profile
```

This builds all necessary dependencies into your application. The only thing left is to follows direction for [configuration](http://orubel.github.io/Beapi-API-Framework/).

This is also available from the grails profiles repo (without doing the build)

Edit your 'USER_HOME/.grails/settings.groovy' file to look like the following:
```

grails {
  profiles {
    repositories {
      maven {
        url =  "https://dl.bintray.com/orubel/profiles/"
        snapshotsEnabled = false
      }
      grailsCentral {
        url = "https://repo.grails.org/grails/core"
        snapshotsEnabled = true
      }
    }
  }
}
                      
```
and then run...

```
grails create-app name_of_your_app --profile org.grails.profiles:beapi-profile:2.0.40
```

If you are having issues, see the bintray profile here: https://bintray.com/beta/#/orubel/profiles/beapi-profile/2.0.40?tab=overview
