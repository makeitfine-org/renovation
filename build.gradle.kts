/*
 * "Renovation": Renovation reporter
 *
 * Copyright 2021
 */

plugins {
    base
    kotlin("jvm") version "1.6.10"
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

tasks.register<GradleBuild>("all") {
    description = "Execute build on modules"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", "clean", "--build-cache")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":frontend:npmInstall", "--build-cache")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":frontend:npmBuild", "--build-cache")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":backend:compileJava", "--build-cache")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", "copyDistToPublic")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":backend:build", "--build-cache")
        }
    }
}

tasks.register<Delete>("removeOldPublic") {
    delete(
        fileTree("${rootProject.rootDir}/backend/src/main/resources/public")
            .matching {
                include("index.html", "favicon.ico", "css/*.*", "js/*.*")
            })
}

tasks.register<Copy>("copyDistToPublic") {
    from("${rootProject.rootDir}/frontend/dist/")
    into("${rootProject.rootDir}/backend/src/main/resources/public")
}

val githookFiles: Array<String> = arrayOf("commit-msg", "pre-push")
val githooks = "${rootProject.rootDir}/.git/hooks"

tasks.register<Copy>("installGitHooks") {
    description = "copy git hooks to .git/hook folder"
    println(description)
    from(
        fileTree("${rootProject.rootDir}/aux/githooks/")
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
