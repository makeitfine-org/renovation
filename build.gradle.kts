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

            testImplementation("io.rest-assured:kotlin-extensions:${properties["restAssuredVersion"]}")
            testImplementation("org.assertj:assertj-core:${properties["assertjVersion"]}")
            testImplementation("org.jetbrains.kotlin:kotlin-test:${properties["kotlinTestVersion"]}")

            ktlint("com.pinterest:ktlint:${properties["ktlintVersion"]}") {
                attributes {
                    attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                }
            }
        }

        val outputDir = "${project.buildDir}/reports/ktlint/"
        val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

        val ktlintCheck by tasks.creating(JavaExec::class) {
            inputs.files(inputFiles)
            outputs.dir(outputDir)

            description = "Check Kotlin code style."
            classpath = ktlint
            mainClass.set("com.pinterest.ktlint.Main")
            args = listOf("src/**/*.kt")
        }

        val ktlintFormat by tasks.creating(JavaExec::class) {
            inputs.files(inputFiles)
            outputs.dir(outputDir)

            description = "Fix Kotlin code style deviations."
            classpath = ktlint
            mainClass.set("com.pinterest.ktlint.Main")
            args = listOf("-F", "src/**/*.kt")
        }

        tasks.withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = java.targetCompatibility.toString()
            }
            // dependsOn(ktlintCheck)
        }

        val integrationTag = "integration"
        val e2eTag = "e2e"
        val minikubeTag = "minikube"

        tasks.withType<Test> {
            useJUnitPlatform {
                excludeTags(integrationTag, e2eTag, minikubeTag)
            }
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

        tasks.register<Test>("minikube") {
            description = "Run minikube tests"

            useJUnitPlatform {
                includeTags("minikube")
            }
        }

        fun testTask(tag: String, mustRunAfter: Any) = tasks.register<Test>(tag) {
            description = "Run $tag tests"

            useJUnitPlatform {
                includeTags("$tag")
            }

            mustRunAfter(mustRunAfter)
        }

        val integrationTest = testTask("integration", tasks.test)
        val e2eTest = testTask("e2e", integrationTest)

        tasks.check {
            dependsOn(integrationTest)
            dependsOn(e2eTest)
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
            commandLine("gradle", "clean")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", "detekt")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":frontend:npmInstall")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":frontend:npmBuild")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":frontend-info:npmInstall")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":frontend-info:npmBuild")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":ng-part:npmInstall")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":ng-part:npmLint")
        }
//        exec { //todo: fix for prod. version
//            workingDir("${rootProject.rootDir}")
//            commandLine("gradle", ":ng-part:npmBuild")
//        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":backend:compileJava")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":info:assemble")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":gateway:assemble")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", "copyDistToPublic")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":backend:build", "-x", "koverVerify")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":temp:build")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":common:build")
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
            commandLine("gradle", "ktlintCheck")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":backend:koverVerify")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", "dependencyCheckAnalyze")
        }
    }
}

val removeImages = "removeImages"

tasks.register<GradleBuild>(removeImages) {
    description = "Remove renovation docker images"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "rmi", "koresmosto/renovation-backend:latest")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "rmi", "koresmosto/renovation-frontend-info:latest")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "rmi", "koresmosto/renovation-info:latest")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "rmi", "koresmosto/renovation-gateway:latest")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "rmi", "koresmosto/renovation-temp:latest")
        }
    }
}

val dockerAndApiTest = "dockerAndApiTest"

tasks.register<GradleBuild>(dockerAndApiTest) {
    description = "Run docker and execute backend api tests"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "compose", "down")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "compose", "build")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("docker", "compose", "up", "-d")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("sleep", "30")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":api-test:clean")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":api-test:build")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":info:build")
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", ":gateway:build")
        }
    }
}

tasks.register<GradleBuild>("all") {
    description = "Execute build on modules, run docker and execute api tests"
    println(description)

    doLast {
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", buildall)
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", checkall)
        }
        exec {
            workingDir("${rootProject.rootDir}")
            commandLine("gradle", dockerAndApiTest)
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
