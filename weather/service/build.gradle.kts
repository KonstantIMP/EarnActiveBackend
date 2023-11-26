import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.shadow)
    id("application")
    kotlin("jvm")
}

application {
    mainClass = "org.kimp.earnactive.weather.service.AppKt"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.grpc)
    implementation(projects.auth.api)
    implementation(projects.common.lock)
    implementation(projects.common.now)
    implementation(projects.weather.api)
    implementation(projects.weather.owa)
    implementation(libs.config)
}

tasks.withType<ShadowJar>().configureEach {
    mergeServiceFiles()
}
