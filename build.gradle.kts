import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/*
 * "Renovation": Renovation reporter
 *
 * Copyright 2021 - 2022
 */

plugins {
    kotlin("jvm")
    id("org.owasp.dependencycheck")
    id("idea")
    id("io.gitlab.arturbosch.detekt")
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
        properties["backendModuleName"],
        properties["apitestModuleName"],
        properties["infoModuleName"],
        properties["commonModuleName"],
        properties["gatewayModuleName"],
        properties["tempModuleName"]
    )

    if (kotlinBasedSubprojects.contains(project.name)) {
        val ktlint by configurations.creating

        apply {
            plugin("kotlin")
            plugin("org.owasp.dependencycheck")
            plugin("idea")
            plugin("io.gitlab.arturbosch.detekt")
        }

        java.sourceCompatibility = JavaVersion.VERSION_17
        java.targetCompatibility = JavaVersion.VERSION_17

        dependencyCheck {
            failBuildOnCVSS = 0f
            suppressionFile = "${rootProject.rootDir}/${properties["owasp_suppressions_file"]}"
        }

        idea {
            module {
                isDownloadJavadoc = true
                isDownloadSources = true
            }
        }

        detekt {
            config = files("${rootProject.rootDir}/aux/detekt/config.yml")
        }

        dependencies {
            if (project.name != properties["commonModuleName"]) {
                api(project(":${properties["commonModuleName"]}"))
            }

            implementation("commons-io:commons-io:${properties["commonsIoVersion"]}")
            implementation("com.fasterxml.jackson:jackson-bom:${properties["jacksonBomVersion"]}")
            implementation("com.fasterxml.jackson.core:jackson-databind:${properties["jacksonDatabindVersion"]}")
            implementation("io.github.microutils:kotlin-logging-jvm:${properties["kotlinLoggingVersion"]}")
            implementation("org.jetbrains.kotlin:kotlin-reflect")

            testImplementation("io.rest-assured:kotlin-extensions:${properties["restAssuredVersion"]}")
            testImplementation("org.assertj:assertj-core:${properties["assertjVersion"]}")
            testImplementation("org.jetbrains.kotlin:kotlin-test:${properties["kotlinTestVersion"]}")

            ktlint("com.pinterest.ktlint:ktlint-cli:${properties["ktlintVersion"]}") {
                attributes {
                    attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                }
            }
        }

        val outputDir = "${project.buildDir}/reports/ktlint/"
        val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

        val ktlintCheck by tasks.registering(JavaExec::class) {
            group = LifecycleBasePlugin.VERIFICATION_GROUP

            inputs.files(inputFiles)
            outputs.dir(outputDir)

            description = "Check Kotlin code style"
            classpath = ktlint
            mainClass.set("com.pinterest.ktlint.Main")
            // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
            args(
                "**/src/**/*.kt",
                //"**.kts",
            )
        }

        tasks.register<JavaExec>("ktlintFormat") {
            group = LifecycleBasePlugin.VERIFICATION_GROUP
            description = "Check Kotlin code style and format"
            classpath = ktlint
            mainClass.set("com.pinterest.ktlint.Main")
            jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
            // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
            args(
                "-F",
                "**/src/**/*.kt",
                //"**.kts",
            )
        }

        // --- compile
        tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = java.targetCompatibility.toString()
            }
            // dependsOn(ktlintCheck)
        }

        // --- test part

        tasks.withType<Test> {
            jvmArgs = mutableListOf("--enable-preview")
            maxParallelForks = Runtime.getRuntime().availableProcessors()

            testLogging {
                showStandardStreams = false
                exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                afterSuite(
                    KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
                        if (desc.parent == null) { // will match the outermost suite
                            println("Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)")
                        }
                    })
                )
            }
        }

        val integrationTestTag = "integrationTest"
        val e2eTestTag = "e2eTest"
        val minikubeTestTag = "minikubeTest"

        fun testTask(tag: String, mustRunAfter: Any?) = tasks.register<Test>(tag) {
            description = "Run $tag tests"

            useJUnitPlatform {
                includeTags("$tag")
            }

            mustRunAfter?.let { mustRunAfter(it) }
        }

        val integrationTest = testTask(integrationTestTag, tasks.test)
        val e2eTest = testTask(e2eTestTag, integrationTest)
        val minikubeTest = testTask(minikubeTestTag, integrationTest)

        tasks.named<Test>("test") {
            description = "Run test tests"

            useJUnitPlatform {
                excludeTags(integrationTestTag, e2eTestTag, minikubeTestTag)
            }
        }

        tasks.check {
            detekt
            dependsOn(ktlintCheck)
            dependsOn(integrationTest)
            dependsOn(e2eTest)
        }
    }
}

val buildAll = "buildAll" // not used camelCase for fast typing buildall

tasks.register<GradleBuild>(buildAll) {
    description = "Execute all tests and build projects (docker compose used)"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "clean")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "detekt")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "ktlintCheck")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "test")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "integrationTest")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "assemble")
        }

        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":frontend:npmInstall")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":frontend:npmBuild")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "copyDistToPublic")
        }

        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":frontend-info:npmInstall")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":frontend-info:npmBuild")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":ng-part:npmInstall")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":ng-part:npmLint")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":ng-part:npmUnitTest")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":node-server:npmInstall")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":node-server:npmLint")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":node-server:npmTest")
        }

        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "compose", "down")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", removeImages)
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "compose", "up", "-d", "--build")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "ps")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("bash", "aux/project/docker_compose_services_starting.bash")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":ng-part:npmE2eTest")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "e2eTest")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "compose", "down")
        }
    }
}

tasks.register<GradleBuild>("ba") { //alias for "buildAll" task
    description = "Alias for $buildAll"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", buildAll)
        }
    }
}

val checkall = "checkall"

tasks.register<GradleBuild>(checkall) {
    description = "Execute checks on modules"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "ktlintCheck")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", ":backend:koverVerify")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", "dependencyCheckAnalyze")
        }
    }
}

val removeImages = "removeImages"

tasks.register<GradleBuild>(removeImages) {
    description = "Remove renovation docker images"
    println(description)

    doLast {
        removeImageLocallyIfExists("koresmosto/renovation-temp:latest")
        removeImageLocallyIfExists("koresmosto/renovation-frontend-info:latest")
        removeImageLocallyIfExists("koresmosto/renovation-info:latest")
        removeImageLocallyIfExists("koresmosto/renovation-gateway:latest")
        removeImageLocallyIfExists("koresmosto/renovation-temp:latest")
    }
}

fun removeImageLocallyIfExists(imageName: String): ExecResult {
    return exec {
        workingDir("${rootProject.rootDir}")
        commandLine("sh", "-c", "[ -z \"$(docker images -q $imageName)\" ] || docker rmi $imageName")
    }
}

tasks.register<GradleBuild>("all") {
    description = "Execute build on modules, run docker and execute api tests"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", buildAll)
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("./gradlew", checkall)
        }
    }
}

tasks.register<Exec>("k8sApiTest") {
    description = "Run backend api tests on kubernetes"
    println(description)

    commandLine("sh", properties["k8sApiTestScript"])
}

tasks.register<Exec>("k8sIngressApiTest") {
    description = "Run backend api tests on kubernetes Ingress"
    println(description)

    commandLine("sh", properties["k8sIngressApiTestScript"])
}

tasks.register<Task>("k8sUploadBackendImage") {
    description = "Upload backend image to kubernetes cluster"
    println(description)

    doLast {
        exec {
            commandLine("sh", properties["backendDeleteScript"])
        }
        exec {
            commandLine("sleep", "30")
        }
        exec {
            commandLine("minikube", "image", "load", properties["backendImageName"])
        }
        exec {
            commandLine("sh", properties["backendDeployScript"])
        }
    }
}

tasks.register<Task>("k8sUploadInfoImage") {
    description = "Upload info image to kubernetes cluster"
    println(description)

    doLast {
        exec {
            commandLine("sh", properties["infoDeleteScript"])
        }
        exec {
            commandLine("sleep", "30")
        }
        exec {
            commandLine("minikube", "image", "load", properties["infoImageName"])
        }
        exec {
            commandLine("sh", properties["infoDeployScript"])
        }
    }
}

tasks.register<Task>("k8sUploadFrontendInfoImage") {
    description = "Upload frontend-info image to kubernetes cluster"
    println(description)

    doLast {
        exec {
            commandLine("sh", properties["frontendInfoDeleteScript"])
        }
        exec {
            commandLine("sleep", "30")
        }
        exec {
            commandLine("minikube", "image", "load", properties["frontendInfoImageName"])
        }
        exec {
            commandLine("sh", properties["frontendInfoDeployScript"])
        }
    }
}

tasks.register<Delete>("removeOldPublic") {
    delete(
        fileTree("${rootProject.rootDir}/backend/src/main/resources/public")
            .matching {
                include("index.html", "favicon.ico", "css/*.*", "js/*.*")
            }
    )
}

tasks.register<Copy>("copyDistToPublic") {
    from("${rootProject.rootDir}/frontend/dist/")
    into("${rootProject.rootDir}/backend/src/main/resources/public")
}

val githookFiles: Array<String> = arrayOf("commit-msg", "pre-push")
val githooks = "${rootProject.rootDir}/.git/hooks"

tasks.register<Copy>("installGitHooks") {

    dependsOn("removeOldGitHooks")

    description = "copy git hooks to .git/hook folder"
    println(description)
    from(
        fileTree("${rootProject.rootDir}/aux/githooks/")
            .matching {
                include(*githookFiles)
            }
    )
    into("$githooks")

    finalizedBy("makeGitHookFilesExecutable")
}

tasks.register<Delete>("removeOldGitHooks") {
    description = "delete old hooks from .git/hook folder"
    println(description)
    delete(
        fileTree("$githooks").matching {
            include(*githookFiles)
        }
    )
}

tasks.register<Exec>("makeGitHookFilesExecutable") {
    description = "make git hooks executable in .git/hook folder"
    println(description)

    workingDir("$githooks")

    commandLine("chmod", "+x", *githookFiles)
}
