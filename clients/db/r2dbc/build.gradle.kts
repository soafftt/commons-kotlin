plugins {
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("java-library")
    kotlin("plugin.spring") version "2.1.10"
}

group = "soft"

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "kotlin-spring")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter:3.4.0")
        api("org.springframework.boot:spring-boot-starter-data-r2dbc:3.4.0")
    }

    tasks.withType<Jar>() {
        enabled = true
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }


    kotlin {
        jvmToolchain(21)
    }
}