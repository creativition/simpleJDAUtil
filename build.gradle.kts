import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "dev.creativition"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    api("net.dv8tion:JDA:5.0.0-beta.8")
    api("com.google.guava:guava:31.1-jre")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}