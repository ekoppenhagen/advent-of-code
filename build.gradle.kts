group = "io.github.ekoppenhagen"
version = "2024"

repositories(RepositoryHandler::mavenCentral)
kotlin { jvmToolchain(21) }

plugins {
    application
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-bom
    kotlin("jvm") version "2.1.0"
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-gradle-plugin
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-formatting
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
}

application {
    mainClass.set("SolveAoCKt")
}

detekt {
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-gradle-plugin
    toolVersion = "1.23.7"
    config.from(file("detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
}
