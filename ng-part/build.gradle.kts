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

tasks.register<NpmTask>("npmLint") {
  args.set(listOf("run", "lint"))
}

tasks.register<NpmTask>("npmE2eTest") {
  args.set(listOf("run", "test.nw.headless"))
}
