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
    implementation(platform(libs.testcontainers.bom))

    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("io.asyncer:r2dbc-mysql:${properties["r2dbcMysqlDriverVersion"]}")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:mysql")
    testImplementation("org.testcontainers:r2dbc")
    testImplementation(libs.quarkus.junit4.mock)
}
