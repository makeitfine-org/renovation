/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

val restAssuredVersion: String by properties
val junitJupiterVersion: String by properties
val commonsIoVersion: String by properties

dependencies {
    testImplementation("io.rest-assured:kotlin-extensions:$restAssuredVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testImplementation("commons-io:commons-io:$commonsIoVersion")
    testImplementation("com.fasterxml.jackson:jackson-bom:${properties["jacksonBomVersion"]}")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:${properties["jacksonDatabindVersion"]}")

}
