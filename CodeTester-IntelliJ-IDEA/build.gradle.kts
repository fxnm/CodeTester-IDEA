import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    id("org.jetbrains.changelog")
}

dependencies {
    implementation(group = "com.squareup.okhttp3", name = "okhttp", version = "4.9.2")
    implementation(group = "io.sentry", name = "sentry", version = "5.2.1") {
        exclude(group = "org.slf4j")
    }
}

changelog {
    version.set(properties("pluginVersion"))
    path.set("${project.parent?.projectDir}/docs/CHANGELOG.md")
    groups.set(listOf("Added", "Changed", "Fixed", "Removed"))
}

tasks {
    patchPluginXml {
        version.set((properties("pluginVersion")))
        sinceBuild.set((properties("pluginSinceBuild")))
        untilBuild.set((properties("pluginUntilBuild")))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
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

        )

        changeNotes.set(provider { changelog.getLatest().toHTML() })
    }

    runIdeForUiTests {
        systemProperty("robot-server.port", properties("remoteRobotPort"))
    }

    downloadRobotServerPlugin {
        version.set(properties("dependency-remote-robot"))
    }

    runPluginVerifier {
        ideVersions.set(
            properties("pluginVerifierIdeVersions")
                .split(',').map(String::trim).filter(String::isNotEmpty)
        )
    }

    publishPlugin {
        dependsOn("patchChangelog")
        token.set((System.getenv("PUBLISH_TOKEN")))
        channels.set(listOf("default"))
    }
}
