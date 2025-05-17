/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

plugins {
    kotlin("plugin.spring")
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("io.rest-assured:kotlin-extensions:${properties["restAssuredVersion"]}")
    implementation("org.jetbrains.kotlin:kotlin-test:${properties["kotlinVersion"]}")
    implementation("org.projectlombok:lombok")
    implementation("com.google.code.gson:gson:${properties["googleGsonVersion"]}");
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        mavenLocal() // Will publish to ~/.m2/repository
    }
}
