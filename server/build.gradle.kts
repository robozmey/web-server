val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    application
}

group = "ru.kpnn"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.athaydes.rawhttp:rawhttp-core:2.5.2")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

application {
    mainClass.set("ru.kpnn.server.MainKt")
}

