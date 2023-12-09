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

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${properties["testcontainersVersion"]}")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-webflux") {
        exclude(group = "com.fasterxml.jackson", module = "jackson-bom")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("mysql:mysql-connector-java")
    runtimeOnly("io.asyncer:r2dbc-mysql:${properties["r2dbcMysqlDriverVersion"]}")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation("io.quarkus:quarkus-junit4-mock:${properties["quarkusJunit4MockVersion"]}")
}
