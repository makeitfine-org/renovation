/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node")
}

tasks.register<NpmTask>("npmBuild") {
    args.set(listOf("run", "build"))
}
