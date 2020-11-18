import org.jetbrains.changelog.closure
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "0.6.3"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "0.6.2"
}

// Import variables from gradle.properties file
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

group = pluginGroup
version = pluginVersion

// Configure project's dependencies
repositories {
    mavenCentral()
    jcenter()
}
dependencies {
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.0")
    implementation(group = "io.sentry", name = "sentry", version = "3.2.0") {
        exclude(group = "org.slf4j")
    }
    implementation(group = "org.projectlombok", name = "lombok", version = "1.18.16")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.16")

    testImplementation(group = "org.junit.jupiter", name = "junit-jupiter", version = "5.7.0")
}

intellij {
    pluginName = pluginName_
    version = platformVersion
    type = platformType
    downloadSources = platformDownloadSources.toBoolean()
    updateSinceUntilBuild = true
    setPlugins("com.intellij.java")
}

changelog {
    path = "${project.projectDir}/docs/CHANGELOG.md"
    groups = listOf("Added", "Changed", "Fixed", "Removed")
}

tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    listOf("compileKotlin", "compileTestKotlin").forEach {
        getByName<KotlinCompile>(it) {
            kotlinOptions.jvmTarget = javaVersion
        }
    }

    patchPluginXml {
        version(pluginVersion)
        sinceBuild(pluginSinceBuild)
        untilBuild(pluginUntilBuild)

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription(
                closure {
                    """
                        <h1>CodeTester-IDEA</h1>
                        <br/>
                        <a href="https://github.com/fxnm/CodeTester-IDEA">GitHub</a> |
                        <a href="https://github.com/fxnm/CodeTester-IDEA/issues">Issues</a>
                        <br/>
                        <br/>""" +

                            File("./docs/README.md").readText().lines().run {
                                val start = "<!-- Plugin description -->"
                                val end = "<!-- Plugin description end -->"

                                if (!containsAll(listOf(start, end))) {
                                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                                }
                                subList(indexOf(start) + 1, indexOf(end))
                            }.joinToString("\n\n").run {
                                markdownToHTML(this)
                            }
                }
        )

        // Get the latest available change notes from the changelog file
        changeNotes(
                closure {
                    changelog.getLatest().toHTML()
                }
        )
    }

    runPluginVerifier {
        ideVersions(pluginVerifierIdeVersions)
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token(System.getenv("PUBLISH_TOKEN"))
        channels("default")
    }
}
