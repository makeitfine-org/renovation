/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

import org.gradle.api.tasks.Exec

tasks.register<Exec>("mci") {
    description = "maven clean install"
    println(description)

    commandLine("mvn", "clean", "install")
}
