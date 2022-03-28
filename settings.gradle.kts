/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    val kotlinPluginVersion: String by settings
    val nodeGradleVersion: String by settings
    val springPluginVersion: String by settings
    val springDependencyPluginVersion: String by settings
    val owaspDependencyPluginVersion: String by settings
    val springframeworkPluginVersion: String by settings
    val jpaPluginVersion: String by settings

    plugins {
        kotlin("jvm") version "${kotlinPluginVersion}"
        id("com.github.node-gradle.node") version "${nodeGradleVersion}"
        kotlin("plugin.spring") version "${springPluginVersion}"
        id("io.spring.dependency-management") version "${springDependencyPluginVersion}"
        id("org.owasp.dependencycheck") version "${owaspDependencyPluginVersion}"
        id("org.springframework.boot") version "${springframeworkPluginVersion}"
        kotlin("plugin.jpa") version "${jpaPluginVersion}"
    }
}

rootProject.name = "renovation"

include("backend")
include("frontend")
include("backend-api-test")
