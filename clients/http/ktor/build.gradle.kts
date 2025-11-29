plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("java-library")
    kotlin("plugin.spring") version "2.2.21"
}

group = "soft"
version = "unspecified"

val ktorClientVersion: String = "3.2.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter")
    // Ktor Client core
    implementation("io.ktor:ktor-client-core:$ktorClientVersion")

    // CIO engine
    implementation("io.ktor:ktor-client-cio:$ktorClientVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorClientVersion")

    // JSON + ContentNegotiation
    implementation("io.ktor:ktor-client-content-negotiation:$ktorClientVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorClientVersion")
    implementation("io.ktor:ktor-serialization-jackson:$ktorClientVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")


    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}