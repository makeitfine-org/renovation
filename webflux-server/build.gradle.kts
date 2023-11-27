/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */
plugins {
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${properties["springframeworkPluginVersion"]}"))
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        exclude(group = "com.fasterxml.jackson", module = "jackson-bom")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}
