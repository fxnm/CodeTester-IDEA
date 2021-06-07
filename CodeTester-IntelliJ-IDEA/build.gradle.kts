import org.jetbrains.changelog.closure
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("org.jetbrains.changelog")
}

dependencies {
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.1")
    implementation(group = "io.sentry", name = "sentry", version = "5.0.1") {
        exclude(group = "org.slf4j")
    }
}

changelog {
    version = properties("pluginVersion")
    path = parent?.projectDir!!.path + "/docs/CHANGELOG.md"
    groups = listOf("Added", "Changed", "Fixed", "Removed")
}

tasks {
    patchPluginXml {
        version(properties("pluginVersion"))
        sinceBuild(properties("pluginSinceBuild"))
        untilBuild(properties("pluginUntilBuild"))

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

        changeNotes(
            closure {
                changelog.getLatest().toHTML()
            }
        )
    }

    runIdeForUiTests {
        systemProperty("robot-server.port", properties("remoteRobotPort"))
    }

    downloadRobotServerPlugin {
        version = properties("dependency-remote-robot")
    }

    runPluginVerifier {
        ideVersions(properties("pluginVerifierIdeVersions"))
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token(System.getenv("PUBLISH_TOKEN"))
        channels("default")
    }
}
