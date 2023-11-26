import com.google.protobuf.gradle.id

plugins {
    alias(libs.plugins.protobuf)
    id("java-library")
    kotlin("jvm")
}

group = "org.kimp.earnactive.weather"

dependencies {
    implementation(libs.protobuf.kt)
    implementation(libs.bundles.grpc)
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    plugins {
        this {
            id("grpc") {
                artifact = libs.protobuf.grpc.java.get().toString()
            }
            id("grpckt") {
                artifact = libs.protobuf.grpc.kotlin.get().toString() + ":jdk8@jar"
            }
        }
    }
    generateProtoTasks {
        all().configureEach {
            plugins {
                this {
                    id("grpc") {}
                    id("grpckt") {}
                }
            }
            builtins {
                this {
                    id("kotlin") {}
                }
            }
        }
    }
}

