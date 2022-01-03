/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2021
 */

import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node") version "3.0.1"
}

tasks.register<NpmTask>("npmBuild") {
    args.set(listOf("run", "build"))
}
