import net.ltgt.gradle.errorprone.errorprone
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

val pluginGroup: String by project
val pluginName_: String by project
val pluginVersion: String by project
val platformType: String by project
val platformVersion: String by project
val platformDownloadSources: String by project
val javaVersion: String by project

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.4.31" apply false
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "0.7.2"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "1.1.2" apply false
    id("net.ltgt.errorprone") version "1.3.0"
}


allprojects {
    group = pluginGroup
    version = pluginVersion

    repositories {
        mavenCentral()
        jcenter()
        maven {
            url = URI("https://jetbrains.bintray.com/intellij-third-party-dependencies")
        }
    }
}

subprojects {

    apply {
        plugin("java")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.intellij")
        plugin("net.ltgt.errorprone")
    }

    if (project.name != "CodeTester-IntelliJ-IDEA") {
        apply {
            dependencies {
                implementation(project(":CodeTester-IntelliJ-IDEA"))
            }
        }
    }

    apply {
        dependencies {
            implementation(group = "org.projectlombok", name = "lombok", version = "1.18.20")
            annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.18")

            testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.7.1")

            errorprone("com.google.errorprone:error_prone_core:2.6.0")
        }
    }

    // Set the compatibility versions to 1.8
    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion

            options.errorprone.disableWarningsInGeneratedCode.set(true)
        }

        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = javaVersion
        }
    }

    intellij {
        pluginName = pluginName_
        type = platformType
        version = platformVersion

        downloadSources = platformDownloadSources.toBoolean()
        updateSinceUntilBuild = true

        setPlugins("com.intellij.java")
    }
}


