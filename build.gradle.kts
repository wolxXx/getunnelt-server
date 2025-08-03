plugins {
    kotlin("jvm") version "1.9.21"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.3.9")
    implementation("io.ktor:ktor-server-cio:2.3.9")
    implementation("io.ktor:ktor-server-websockets:2.3.9")
    implementation("io.ktor:ktor-server-metrics-micrometer:2.3.9")
    implementation("io.micrometer:micrometer-registry-prometheus:1.12.3")
    implementation("io.lettuce:lettuce-core:6.7.1.RELEASE")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

application {
    mainClass.set("MainKt")
}
