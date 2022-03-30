import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * "Renovation": Renovation reporter
 *
 * Copyright 2021
 */

plugins {
    kotlin("jvm")
    id("org.owasp.dependencycheck")
}

group = properties["projectGroup"]!!
version = properties["projectVersion"]!!

allprojects {
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    apply {
        from("${rootProject.rootDir}/extra.gradle.kts")
    }

    val kotlinBasedSubprojects = arrayOf(
        properties["moduleBackendName"],
        properties["moduleBackendapitestName"]
    )

    if (kotlinBasedSubprojects.contains(project.name)) {
        apply {
            plugin("kotlin")
            plugin("org.owasp.dependencycheck")
        }

        java.sourceCompatibility = JavaVersion.VERSION_17
        java.targetCompatibility = JavaVersion.VERSION_17

        dependencyCheck {
            failBuildOnCVSS = 0f
        }

        dependencies {
            implementation("commons-io:commons-io:${properties["commonsIoVersion"]}")
            implementation("com.fasterxml.jackson:jackson-bom:${properties["jacksonBomVersion"]}")
            implementation("com.fasterxml.jackson.core:jackson-databind:${properties["jacksonDatabindVersion"]}")

            testImplementation("org.junit.jupiter:junit-jupiter:${properties["junitJupiterVersion"]}")
            testImplementation("io.rest-assured:kotlin-extensions:${properties["restAssuredVersion"]}")
            testImplementation("org.assertj:assertj-core:${properties["assertjVersion"]}")
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = java.targetCompatibility.toString()
            }
        }

        val smokeTag = "smoke"
        val healthCheckTag = "healthCheck"
        val integrationTag = "integration"
        val functionalTag = "functional"

        tasks.withType<Test> {
            useJUnitPlatform {
                excludeTags("$smokeTag", "$healthCheckTag", "$integrationTag", "$functionalTag")
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

        fun testTask(tag: String, mustRunAfter: Any) = tasks.register<Test>(tag) {
            description = "Run $tag tests"

            useJUnitPlatform {
                includeTags("$tag")
            }

            mustRunAfter(mustRunAfter)
        }

        val healthCheckTest = testTask("healthCheck", tasks.test)
        val smokeTest = testTask("smoke", healthCheckTest)
        val intTest = testTask("integration", smokeTest)
        val functionalTest = testTask("functional", intTest)

        tasks.check {
            dependsOn(healthCheckTest)
            dependsOn(smokeTest)
            dependsOn(intTest)
            dependsOn(functionalTest)
        }
    }
}

val buildall = "buildall"

tasks.register<GradleBuild>(buildall) {
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

tasks.register<GradleBuild>("all") {
    description = "Execute build on modules, run end and execute api tests"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", buildall)
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker-compose", "down")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker-compose", "build")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker-compose", "up", "-d")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("sleep", "7")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":backend-api-test:clean", "--build-cache")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":backend-api-test:build", "--build-cache")
        }
    }
}

//todo: change "all" to "buildall" (add backend-api-test testClasses),
// but to "all" add docker-compose up and backend-api-test test run

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
