import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "kotlin")

    kotlin {
        jvmToolchain(21)
    }

    repositories {
        mavenCentral()
    }
}

