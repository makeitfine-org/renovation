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
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${properties["springCloudDependenciesVersion"]}")
    }
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${properties["springframeworkPluginVersion"]}"))
    implementation("org.springframework.cloud:spring-cloud-starter-vault-config")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.github.microutils:kotlin-logging-jvm:${properties["kotlinLoggingVersion"]}")
    implementation("org.apache.commons:commons-lang3:${properties["commonsLang3Version"]}")
    implementation("com.google.guava:guava:${properties["guavaVersion"]}")
}
