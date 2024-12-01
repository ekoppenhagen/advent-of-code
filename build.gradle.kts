group = "io.github.ekoppenhagen"
version = "2023.1.0"

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

dependencies {
    val kotestVersion = "5.9.1" // https://mvnrepository.com/artifact/io.kotest/kotest-runner-junit5
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
}

detekt {
    // https://mvnrepository.com/artifact/io.gitlab.arturbosch.detekt/detekt-gradle-plugin
    toolVersion = "1.23.7"
    config.from(file("config/detekt.yml"))
    buildUponDefaultConfig = true
    allRules = false
}

tasks.test {
    useJUnitPlatform()
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter
            useJUnitJupiter("5.11.3")
        }
    }
}
