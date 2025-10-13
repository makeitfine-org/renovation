/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

tasks.register<Exec>("mci") {
    description = "maven clean install"
    println(description)

    commandLine("mvn", "clean", "install")
}

tasks.register<Exec>("mavenClean") {
    description = "maven clean"
    println(description)

    commandLine("mvn", "clean")
}
