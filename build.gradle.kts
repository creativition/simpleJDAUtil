import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

plugins {
    kotlin("jvm") version "1.7.10"
    id("maven-publish")
    id("java-library")
}

group = "dev.creativition"
version = "0.0.1"
val localMavenRepository = "file:///D:/myworks/github/maven/"

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "dev.creativition"
            artifactId = "simplejdautil"
            version

            from(components["java"])
        }
    }
    repositories {
        maven(
            url = URI(localMavenRepository)
        )
    }
}

dependencies {
    api("net.dv8tion:JDA:5.0.0-beta.8")
    api("com.google.guava:guava:31.1-jre")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}