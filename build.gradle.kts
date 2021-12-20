/*
 * "Renovation": Renovation reporter
 *
 * Copyright 2021
 */

plugins {
    base
    kotlin("jvm") version "1.5.31"
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
}

allprojects {
    group = "renovation"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

tasks.register<Exec>("all") {
    description = "Execute build on modules"
    println(description)

    workingDir(".")

    commandLine(
        "gradle",
        ":backend:clean", "--build-cache",
        ":backend:build", "--build-cache",
        ":frontend:npmInstall", "--build-cache"
    )
}

val githookFiles: Array<String> = arrayOf("commit-msg", "pre-push")
val githooks = "${rootProject.rootDir}/.git/hooks"

tasks.register<Copy>("installGitHooks") {
    description = "copy git hooks to .git/hook folder"
    println(description)
    from(
        fileTree("${rootProject.rootDir}/gradle/githooks/")
            .matching {
                include(*githookFiles)
            })
    into("$githooks")

//    dependsOn("removeOldGitHooks")
    finalizedBy("makeGitHookFilesExecutable")
}

tasks.register<Delete>("removeOldGitHooks") {
    description = "delete old hooks from .git/hook folder"
    println(description)
    delete(fileTree("$githooks").matching {
        include(*githookFiles)
    })
}

tasks.register<Exec>("makeGitHookFilesExecutable") {
    description = "make git hooks executable in .git/hook folder"
    println(description)

    workingDir("$githooks")

    commandLine("chmod", "+x", *githookFiles)
}
