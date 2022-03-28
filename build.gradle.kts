/*
 * "Renovation": Renovation reporter
 *
 * Copyright 2021
 */

plugins {
    base
    kotlin("jvm") version "1.6.10"
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    id("org.owasp.dependencycheck") version "7.0.1" apply true
}

group = "renovation"
version = "0.0.1-SNAPSHOT"

//dependency versions
//todo: create versions.gradle.kts with dep versions

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    apply {
        plugin("org.owasp.dependencycheck")
        plugin("kotlin")
        from("${rootProject.rootDir}/versions.gradle.kts")
    }

    java.sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = JavaVersion.VERSION_17

    dependencyCheck {
        failBuildOnCVSS = 0f
    }

    dependencies {
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform {
            excludeTags("smoke", "healthCheck", "integration", "functional")
        }
        jvmArgs = mutableListOf("--enable-preview")
        maxParallelForks = Runtime.getRuntime().availableProcessors()

        testLogging {
            showStandardStreams = false
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
                if (desc.parent == null) { // will match the outermost suite
                    println("Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)")
                }
            }))
        }
    }

    val healthCheckTest = tasks.register<Test>("healthCheck") {
        description = "Run healthCheck tests"

        useJUnitPlatform {
            includeTags("healthCheck")
        }

        mustRunAfter(tasks.test)
    }

    val smokeTest = tasks.register<Test>("smokeTest") {
        description = "Run smoke tests"

        useJUnitPlatform {
            includeTags("smoke")
        }

        mustRunAfter(healthCheckTest)
    }

    val intTest = tasks.register<Test>("intTest") {
        description = "Run integration tests"

        useJUnitPlatform {
            includeTags("integration")
        }

        mustRunAfter(smokeTest)
    }

    val functionalTest = tasks.register<Test>("functionalTest") {
        description = "Run functional tests"

        useJUnitPlatform {
            includeTags("functional")
        }

        mustRunAfter(intTest)
    }

    tasks.check {
        dependsOn(healthCheckTest)
        dependsOn(smokeTest)
        dependsOn(intTest)
        dependsOn(functionalTest)
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
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", "dependencyCheckAnalyze")
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
