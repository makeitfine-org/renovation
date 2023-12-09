/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal() // todo: think of deletion
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    val kotlinVersion: String by settings
    val nodeGradleVersion: String by settings
    val springDependencyPluginVersion: String by settings
    val owaspDependencyPluginVersion: String by settings
    val springframeworkPluginVersion: String by settings
    val koverPluginVersion: String by settings
    val dgsCodegenVersion: String by settings
    val detektVersion: String by settings
    val ktorPluginVersion: String by settings
    val champeauJmhVersion: String by settings


    plugins {
        kotlin("jvm") version "$kotlinVersion"
        kotlin("plugin.spring") version "$kotlinVersion"
        kotlin("plugin.jpa") version "$kotlinVersion"
        id("org.jetbrains.kotlin.plugin.serialization") version "$kotlinVersion"
        id("com.github.node-gradle.node") version "$nodeGradleVersion"
        id("io.spring.dependency-management") version "$springDependencyPluginVersion"
        id("org.owasp.dependencycheck") version "$owaspDependencyPluginVersion"
        id("org.springframework.boot") version "$springframeworkPluginVersion"
        id("org.jetbrains.kotlinx.kover") version "$koverPluginVersion"
        id("com.netflix.dgs.codegen") version "$dgsCodegenVersion"
        id("io.gitlab.arturbosch.detekt") version "$detektVersion"
        id("io.ktor.plugin") version "$ktorPluginVersion"
        id("me.champeau.jmh") version "$champeauJmhVersion"
    }
}

rootProject.name = "renovation"

val backendModuleName: String by settings
val frontendModuleName: String by settings
val frontendInfoModuleName: String by settings
val apitestModuleName: String by settings
val mockapiModuleName: String by settings
val infoModuleName: String by settings
val commonModuleName: String by settings
val gatewayModuleName: String by settings
val tempModuleName: String by settings
val ngPartModuleName: String by settings
val nodeServerModuleName: String by settings
val webfluxServerModuleName: String by settings
val ktorServerModuleName: String by settings

include(backendModuleName)
include(frontendModuleName)
include(frontendInfoModuleName)
include(apitestModuleName)
include(mockapiModuleName)
include(infoModuleName)
include(commonModuleName)
include(gatewayModuleName)
include(tempModuleName)
include(ngPartModuleName)
include(nodeServerModuleName)
include(webfluxServerModuleName)
include(ktorServerModuleName)
