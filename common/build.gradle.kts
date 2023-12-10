/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("io.rest-assured:kotlin-extensions:${properties["restAssuredVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-test:${properties["kotlinVersion"]}")
}
