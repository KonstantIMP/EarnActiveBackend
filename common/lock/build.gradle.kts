plugins {
    id("java-library")
    kotlin("jvm")
}

dependencies {
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(kotlin("test"))
    testImplementation(libs.hamkrest)
}

tasks.test {
    useJUnitPlatform()
}

