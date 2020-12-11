import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val pluginGroup: String by project
val pluginName_: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project
val pluginVerifierIdeVersions: String by project

val platformType: String by project
val platformVersion: String by project
val platformDownloadSources: String by project

val javaVersion: String by project

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21" apply false
}


allprojects {
    group = pluginGroup
    version = pluginVersion

    repositories {
        mavenCentral()
        jcenter()
    }

}

subprojects {
    // Set the compatibility versions to 1.8
    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = javaVersion
        }
    }
}
