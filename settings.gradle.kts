pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = ("EarnActive")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    "common:lock",
    "common:now",
    "common:utils",

    "db",

    "auth:api",
    "auth:sms",
    "auth:sms-cli",
    "auth:controller",

    "promocodes:api",
    "promocodes:service",

    "weather:api",
    "weather:owa",
    "weather:owa-cli",
    "weather:service",

    "facade",
)
