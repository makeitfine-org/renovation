/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.spring.dependency-management")
    kotlin("plugin.spring") version "1.6.10"
    id("org.springframework.boot") version "2.6.5"
    kotlin("plugin.jpa") version "1.6.10"
}
//todo: to be deleted after api-tests module complete (and test moving)
val restAssuredVersion : String by extra
val testcontainersVersion = "1.16.2"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${testcontainersVersion}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.postgresql:postgresql:42.3.1")

    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "com.fasterxml.jackson", module = "jackson-bom")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("org.springframework.boot:spring-boot-starter-actuator") {
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("com.fasterxml.jackson:jackson-bom:2.13.2.20220324")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2.1")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.liquibase:liquibase-core:4.9.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
        exclude("org.hibernate:hibernate-core")
    }
    implementation("org.hibernate:hibernate-core:5.6.5.Final")
    //    todo: to be deleted (if possible)
    runtimeOnly("com.h2database:h2:2.1.210")

    testImplementation("io.rest-assured:kotlin-extensions:${restAssuredVersion}")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}
