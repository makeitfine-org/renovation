import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

plugins {
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("me.champeau.jmh")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        suppressWarnings = true
    }
}

dependencies {
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(platform(libs.spring.boot.dependencies))

    implementation("org.springframework.cloud:spring-cloud-starter-vault-config")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.apache.commons:commons-lang3:${properties["commonsLang3Version"]}")
    implementation("com.google.guava:guava:${properties["guavaVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation(libs.mockk)
}

jmh {
//    includes.add(".*init1000")
//    excludes.add(".*init10000")
//    threads.set(2)
}
