/*
 * "Renovation": Renovation reporter
 *
 * Copyright 2021 - 2022
 */
/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

plugins {
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("com.netflix.dgs.codegen")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${properties["testcontainersVersion"]}")
    }
}

dependencies {
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:${properties["dgsPlatformDependenciesVersion"]}"))
    implementation("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-validation")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude(group = "com.fasterxml.jackson", module = "jackson-bom")
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-databind")
    }
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-resource-server")
    implementation("org.springframework.security:spring-security-oauth2-jose")

    testImplementation("org.junit.jupiter:junit-jupiter:${properties["junitJupiterVersion"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit")
        exclude(group = "org.junit.jupiter")
        exclude(group = "org.junit.vintage")
        exclude(group = "org.mockito")
    }
    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("org.testcontainers:junit-jupiter") {
        exclude("org.junit.jupiter")
    }
    testImplementation("org.testcontainers:mongodb")
}

tasks.generateJava {
    generateClient = true
    packageName = "renovation.info.generated.dgs"
    typeMapping = mutableMapOf(
        "Age" to "kotlin.Int"
    )
}
