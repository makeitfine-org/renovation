/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node")
}

tasks.register<NpmTask>("npmBuild") {
    args.set(listOf("run", "build"))
}

tasks.register<NpmTask>("npmClean") {
    args.set(listOf("run", "clean"))
}
