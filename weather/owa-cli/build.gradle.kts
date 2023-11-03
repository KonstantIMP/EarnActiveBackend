plugins {
    id("application")
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.weather.owa)
}
