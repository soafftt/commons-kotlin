plugins {
    kotlin("jvm")
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.0"
}

group = "soft"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.4.0")
    api("org.springframework.data:spring-data-r2dbc:3.4.0")

    implementation("io.asyncer:r2dbc-mysql:1.4.0")
    implementation("io.r2dbc:r2dbc-proxy:1.1.6")
    implementation("io.r2dbc:r2dbc-pool:1.0.2")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}