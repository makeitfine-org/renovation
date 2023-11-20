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

tasks.register<NpmTask>("npmUnitTest") { //make part of e2eTest gradle  task
  args.set(listOf("run", "unit.test"))
}

tasks.register<NpmTask>("npmE2eTest") { //make part of e2eTest gradle  task
  args.set(listOf("run", "e2e.test"))
}
