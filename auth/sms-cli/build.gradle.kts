plugins {
    id("application")
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.auth.sms)
}
