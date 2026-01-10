/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

tasks.register<Exec>("mci") {
    description = "maven clean install"
    println(description)

    commandLine("mvn", "clean", "install")
}

tasks.register<Exec>("clean") {
    description = "Run `mvn clean`"

    commandLine("mvn", "clean")
}
