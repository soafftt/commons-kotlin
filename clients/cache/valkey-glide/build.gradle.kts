plugins {
    id("org.springframework.boot") version "4.0.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("java-library")
    id("com.google.osdetector") version "1.7.3"
    kotlin("plugin.spring") version "2.2.21"
}

group = "soft"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation(group = "io.valkey", name = "valkey-glide", version="2.2.0", classifier = "${osdetector.classifier}")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.10.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:4.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux:4.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(24)
}