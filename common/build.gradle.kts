dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.3")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:2.1.20")
}

tasks.test {
    useJUnitPlatform()
}

