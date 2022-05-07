/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

plugins {
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    kotlin("plugin.jpa")
    id("org.jetbrains.kotlinx.kover")
}

// configurations.all {
//    exclude(group = "junit", module = "junit")
// }

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${properties["testcontainersVersion"]}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.postgresql:postgresql:${properties["postgresqlVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "com.fasterxml.jackson", module = "jackson-bom")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator") {
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core:${properties["liquibaseVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
        exclude("org.hibernate:hibernate-core")
    }
    implementation("org.hibernate:hibernate-core:${properties["hibernateVersion"]}")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("io.github.microutils:kotlin-logging-jvm:${properties["kotlinLoggingVersion"]}")

    testImplementation("com.h2database:${properties["h2Version"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit")
        exclude(group = "org.junit.jupiter")
        exclude(group = "org.junit.vintage")
        exclude(group = "org.mockito")
    }
    testImplementation("org.testcontainers:junit-jupiter") {
        exclude("org.junit.jupiter")
    }
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.mockk:mockk:${properties["mockkVersion"]}")
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
