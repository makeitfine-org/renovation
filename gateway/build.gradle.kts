/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

plugins {
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${properties["springCloudDependenciesVersion"]}")
    }
}


dependencies {
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter") {
        exclude(group = "com.fasterxml.jackson", module = "jackson-bom")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("io.mockk:mockk:${properties["mockkVersion"]}")
    implementation("org.springframework.cloud:spring-cloud-starter-function-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit")
        exclude(group = "org.junit.jupiter")
        exclude(group = "org.junit.vintage")
        exclude(group = "org.mockito")
    }
}
