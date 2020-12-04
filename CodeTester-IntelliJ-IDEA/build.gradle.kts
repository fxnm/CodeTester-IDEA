import org.jetbrains.changelog.closure
import org.jetbrains.changelog.markdownToHTML

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm")
    // gradle-intellij-plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
    id("org.jetbrains.intellij") version "0.6.5"
    // gradle-changelog-plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
    id("org.jetbrains.changelog") version "0.6.2"
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
    pluginName = project.parent?.ext?.get("pluginName_") as String
    version = project.parent?.ext?.get("platformVersion") as String
    type = project.parent?.ext?.get("platformType") as String?
    downloadSources = project.parent?.ext?.get("platformDownloadSources").toString().toBoolean()
    updateSinceUntilBuild = true

    setPlugins("com.intellij.java")
}

changelog {
    path = parent?.projectDir!!.path + "/docs/CHANGELOG.md"
    groups = listOf("Added", "Changed", "Fixed", "Removed")
}

tasks {

    patchPluginXml {
        version(project.parent?.ext?.get("pluginVersion") as String)
        sinceBuild(project.parent?.ext?.get("pluginSinceBuild") as String)
        untilBuild(project.parent?.ext?.get("pluginUntilBuild") as String)

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

                        File(parent?.projectDir!!.path + "/docs/README.md").readText().lines().run {
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
        ideVersions(project.parent?.ext?.get("pluginVerifierIdeVersions") as String?)
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token(System.getenv("PUBLISH_TOKEN"))
        channels("default")
    }
}
