import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)

    id("application")
    kotlin("jvm")

    alias(libs.plugins.kotlinx.spring)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.bundles.spring)
    implementation(projects.promocodes.api)
    implementation(projects.weather.api)
    implementation(projects.auth.api)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.grpc.core)
}
