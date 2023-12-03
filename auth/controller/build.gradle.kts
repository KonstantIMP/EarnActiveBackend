plugins {
    alias(libs.plugins.shadow)
    id("application")
    kotlin("jvm")
}

application {
    mainClass = "org.kimp.earnactive.auth.controller.AppKt"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.logging)
    implementation(libs.bundles.grpc)
    implementation(libs.bundles.jwt)
    implementation(projects.common.lock)
    implementation(projects.common.now)
    implementation(projects.common.utils)
    implementation(projects.auth.api)
    implementation(projects.auth.sms)
    implementation(projects.db)
    implementation(libs.config)
    implementation(libs.hikari)
    implementation(libs.jedis)
    implementation(libs.jooq)
    implementation(libs.psql)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(kotlin("test"))
    testImplementation(libs.hamkrest)
}

tasks.test {
    useJUnitPlatform()
}
