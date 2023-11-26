plugins {
    alias(libs.plugins.kotlinx.serialization)
    id("java-library")
    kotlin("jvm")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.ktor.client)
}
