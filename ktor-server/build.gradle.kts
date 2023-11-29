/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:${properties["logbackVersion"]}")
    implementation("io.ktor:ktor-server-config-yaml")
    implementation("io.ktor:ktor-server-cors-jvm")
    implementation("io.ktor:ktor-network-tls-certificates")

    testImplementation("io.ktor:ktor-server-tests-jvm")
}

project.ext["development"] = true

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    //VM options: -Dio.ktor.development=true
    val isDevelopment: Boolean = project.ext.has("development") && project.ext["development"] == true
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.test {
    useJUnitPlatform()
}

ktor {
    docker {
        localImageName.set("koresmosto/renovation-ktor-server")
        imageTag.set("latest")
    }
}
