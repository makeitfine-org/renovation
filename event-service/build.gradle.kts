/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2024
 */

tasks.register<Exec>("mi") {
    description = "maven install"
    println(description)

    commandLine("mvn", "install")
}

tasks.register<Exec>("mavenClean") {
    description = "maven clean"
    println(description)

    commandLine("mvn", "clean")
}
