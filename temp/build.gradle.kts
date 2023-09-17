/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${properties["springframeworkPluginVersion"]}"))
    implementation("org.springframework.boot:spring-boot-starter-web")
}