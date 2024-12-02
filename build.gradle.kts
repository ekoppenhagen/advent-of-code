group = "io.github.ekoppenhagen"
version = "2023.7.0"

repositories(RepositoryHandler::mavenCentral)
kotlin { jvmToolchain(21) }

plugins {
    application
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-bom
    kotlin("jvm") version "2.0.21"
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-gradle-plugin
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

application {
    mainClass.set("SolveAoCKt")
}

detekt {
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-gradle-plugin
    toolVersion = "1.23.7"
    config.from(file("config/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
}
