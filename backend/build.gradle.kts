/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

plugins {
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    kotlin("plugin.jpa")
    id("org.jetbrains.kotlinx.kover")
}

dependencies {
    implementation(platform(libs.testcontainers.bom))

    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.postgresql:postgresql:${properties["postgresqlVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-web") {
//        todo: remove jackson exlude
        exclude(group = "com.fasterxml.jackson", module = "jackson-bom")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator") {
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
        exclude("org.hibernate:hibernate-core")
    }
    implementation("org.hibernate:hibernate-core:${properties["hibernateVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    // implementation("org.keycloak.bom:keycloak-adapter-bom:${properties["keycloakVersion"]}")
//    implementation("org.keycloak:keycloak-spring-boot-starter:${properties["keycloakVersion"]}")

    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    testImplementation("com.h2database:h2:${properties["h2Version"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation(libs.mockk)
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("com.github.dasniko:testcontainers-keycloak:${properties["testcontainersKeycloakVersion"]}")
}

tasks.koverVerify {
    excludes = listOf(
        "renovation.backend.RenovationApplicationKt",
        "renovation.backend.web.controller.RouteController",
    )

    rule {
        name = "Minimal line coverage rate in percents"
        bound {
            minValue = 100
        }
    }
}

tasks.koverHtmlReport {
    excludes = listOf(
        "renovation.backend.RenovationApplicationKt",
        "renovation.backend.web.controller.RouteController",
    )
}
