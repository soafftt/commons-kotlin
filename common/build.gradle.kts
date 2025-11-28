import jdk.jfr.internal.JVM.include

plugins {
    id("java-library")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("tools.jackson.module:jackson-module-kotlin:3.0.2")

    implementation("tools.jackson.datatype:jackson-datatype-jsr310:3.0.0-rc2") {
        exclude(group = "com.fasterxml.jackson.core", module = "jackson-annotations")
    }
    implementation("com.fasterxml.jackson.core:jackson-annotations:3.0-rc5")

    implementation("com.nimbusds:nimbus-jose-jwt:10.6")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:2.1.20")
}

tasks.test {
    useJUnitPlatform()
}

