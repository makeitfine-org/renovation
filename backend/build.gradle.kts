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
}
//todo: to be deleted after api-tests module complete (and test moving)

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
    implementation("com.fasterxml.jackson:jackson-bom:${properties["jacksonBomVersion"]}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${properties["jacksonDatabindVersion"]}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("commons-io:commons-io:${properties["commonsIoVersion"]}")
    implementation("org.liquibase:liquibase-core:${properties["liquibaseVersion"]}")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
        exclude("org.hibernate:hibernate-core")
    }
    implementation("org.hibernate:hibernate-core:${properties["hibernateVersion"]}")
    //    todo: to be deleted (if possible)
    runtimeOnly("com.h2database:${properties["h2Version"]}")

    testImplementation("io.rest-assured:kotlin-extensions:${properties["restAssuredVersion"]}")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
}
