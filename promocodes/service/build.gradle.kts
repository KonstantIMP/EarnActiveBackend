plugins {
    alias(libs.plugins.shadow)
    id("application")
    kotlin("jvm")
}

application {
    mainClass = "org.kimp.earnactive.promocodes.service.AppKt"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.grpc)
    implementation(projects.auth.api)
    implementation(projects.common.lock)
    implementation(projects.common.now)
    implementation(projects.common.utils)
    implementation(projects.promocodes.api)
    implementation(libs.config)
    implementation(libs.hikari)
    implementation(libs.jooq)
    implementation(libs.psql)
    implementation(projects.db)
}
