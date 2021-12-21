/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("io.spring.dependency-management")
    kotlin("plugin.spring") version "1.5.31"
    id("org.springframework.boot") version "2.5.6"
    //    todo: to be deleted
    kotlin("plugin.jpa") version "1.5.31"
}

val restAssuredVersion = "4.4.0"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.postgresql:postgresql:42.3.1")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    testCompileOnly("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")

    //    todo: to be deleted
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    //    todo: to be deleted
    runtimeOnly("com.h2database:h2")

    testImplementation("io.rest-assured:kotlin-extensions:${restAssuredVersion}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "15"
    }
}

tasks.withType<Test> {
    useJUnitPlatform() {
        excludeTags("smoke", "healthCheck", "integration")
    }
    jvmArgs = mutableListOf("--enable-preview")
    maxParallelForks = Runtime.getRuntime().availableProcessors()

    testLogging {
        showStandardStreams = false
        exceptionFormat = FULL
        afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
            if (desc.parent == null) { // will match the outermost suite
                println("Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)")
            }
        }))
    }
}

val healthCheckTest = tasks.register<Test>("healthCheck") {
    description = "Run healthCheck tests"

    useJUnitPlatform() {
        includeTags("healthCheck")
    }

    mustRunAfter(tasks.test)
}

val smokeTest = tasks.register<Test>("smokeTest") {
    description = "Run smoke tests"

    useJUnitPlatform() {
        includeTags("smoke")
    }

    mustRunAfter(healthCheckTest)
}

val intTest = tasks.register<Test>("intTest") {
    description = "Run integration tests"

    useJUnitPlatform() {
        includeTags("integration")
    }

    mustRunAfter(smokeTest)
}

tasks.check {
    dependsOn(healthCheckTest)
    dependsOn(smokeTest)
    dependsOn(intTest)
}
