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

    "db",

    "auth:api",
    "auth:sms",
    "auth:sms-cli",
    "auth:controller",

    "weather:api",
    "weather:owa",
    "weather:owa-cli",
    "weather:service",

    "facade",
)
