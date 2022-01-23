import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import java.net.URI

plugins {
    id("org.springframework.boot") version "2.6.2"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"

    `java-library`
    `maven-publish`
}

group = "com.soft"
version = findProperty("commons_version") as String

java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    compileOnly("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("software.amazon.awssdk:dynamodb")
    implementation("com.google.guava:guava:31.0.1-jre")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.lettuce:lettuce-core:6.1.5.RELEASE")
    implementation("net.logstash.logback:logstash-logback-encoder:7.0.1")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")


    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

dependencyManagement {
    imports {
        mavenBom("software.amazon.awssdk:bom:2.17.86")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
    builder = "paketobuildpacks/builder:tiny"
    environment = mapOf("BP_NATIVE_IMAGE" to "true")
}

tasks.jar {
    enabled = true
    exclude("*.yml")
}

tasks.bootJar {
    enabled = false
}

publishing {
    repositories {
        maven {
            val liveRepo = findProperty("nexus_repository") as String
            val snapshotRepo = findProperty("nexus_snapshot_repository") as String
            //var version = findProperty("") as String
            val uri = if(version.toString().endsWith("SNAPSHOT")) { snapshotRepo } else { liveRepo }

            url = URI(uri)
            credentials {
                username = findProperty("nexus_id") as String
                password = findProperty("nexus_password") as String
            }
        }
    }

    publications {
        create<MavenPublication>("commons") {
            groupId = group as String
            artifactId = "commons"

            from(components["java"])
        }
    }
}