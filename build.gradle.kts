import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.2.21"
}

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "kotlin")

    kotlin {
        jvmToolchain(24)
    }

    repositories {
        mavenCentral()
    }
}

