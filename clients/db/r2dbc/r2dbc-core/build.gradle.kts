dependencies {
    implementation("io.asyncer:r2dbc-mysql:1.4.1")
    api("io.r2dbc:r2dbc-proxy:1.1.6.RELEASE")
    api("io.r2dbc:r2dbc-pool:1.0.2.RELEASE")

    api("org.jetbrains.exposed:exposed-core:1.0.0-rc-4")
    api("org.jetbrains.exposed:exposed-r2dbc:1.0.0-rc-4")

    testImplementation(kotlin("test"))
}