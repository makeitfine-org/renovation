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
    implementation(libs.spring.doc)
    implementation(libs.spring.doc.starter.common)

    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.postgresql:postgresql:${properties["postgresqlVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
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

    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    implementation("org.springframework.session:spring-session-data-redis")

    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation(libs.mockk)
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation(libs.testcontainers.keycloak)
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
