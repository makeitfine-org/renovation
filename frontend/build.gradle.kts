/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node")
    kotlin("jvm") apply false
}

tasks.register<NpmTask>("npmBuild") {
    args.set(listOf("run", "build"))
}

dependencyCheck {
    skip = true
}
