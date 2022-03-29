/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

val restAssuredVersion: String by properties
val junitJupiterVersion: String by properties

dependencies {
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}
