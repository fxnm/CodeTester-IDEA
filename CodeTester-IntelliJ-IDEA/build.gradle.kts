import org.jetbrains.changelog.closure
import org.jetbrains.changelog.markdownToHTML

val remoteRobotPort: String by project
val pluginVersion: String by project
val pluginSinceBuild: String by project
val pluginUntilBuild: String by project
val pluginVerifierIdeVersions: String by project

plugins {
    id("org.jetbrains.changelog")
}

dependencies {
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.0")
    implementation(group = "io.sentry", name = "sentry", version = "3.2.0") {
        exclude(group = "org.slf4j")
    }
}

changelog {
    path = parent?.projectDir!!.path + "/docs/CHANGELOG.md"
    groups = listOf("Added", "Changed", "Fixed", "Removed")
}

tasks {
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

    runIdeForUiTests {
        systemProperty("robot-server.port", remoteRobotPort)
    }

    downloadRobotServerPlugin {
        version = "0.10.2"
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
