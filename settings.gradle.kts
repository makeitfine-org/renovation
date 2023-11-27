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

    val kotlinPluginVersion: String by settings
    val nodeGradleVersion: String by settings
    val springPluginVersion: String by settings
    val springDependencyPluginVersion: String by settings
    val owaspDependencyPluginVersion: String by settings
    val springframeworkPluginVersion: String by settings
    val jpaPluginVersion: String by settings
    val koverPluginVersion: String by settings
    val dgsCodegenVersion: String by settings
    val detektVersion: String by settings


    plugins {
        kotlin("jvm") version "$kotlinPluginVersion"
        id("com.github.node-gradle.node") version "$nodeGradleVersion"
        kotlin("plugin.spring") version "$springPluginVersion"
        id("io.spring.dependency-management") version "$springDependencyPluginVersion"
        id("org.owasp.dependencycheck") version "$owaspDependencyPluginVersion"
        id("org.springframework.boot") version "$springframeworkPluginVersion"
        kotlin("plugin.jpa") version "$jpaPluginVersion"
        id("org.jetbrains.kotlinx.kover") version "$koverPluginVersion"
        id("com.netflix.dgs.codegen") version "$dgsCodegenVersion"
        id("io.gitlab.arturbosch.detekt") version "$detektVersion"
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
