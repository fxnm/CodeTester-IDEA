fun properties(key: String) = project.findProperty(key).toString()

dependencies {
    testImplementation(gradleApi())

    testImplementation(
        group = "com.intellij.remoterobot",
        name = "remote-robot",
        version = properties("dependency-remote-robot")
    )

    testImplementation(
        group = "com.intellij.remoterobot",
        name = "remote-fixtures",
        version = properties("dependency-remote-fixtures")
    )
}

tasks {
    test {
        // don't run gui tests as part of check
        enabled = false
    }

    register<Test>("runUiTest") {
        // we don't want to cache the results of this.
        outputs.upToDateWhen { false }

        systemProperty("robot-server.port", properties("remoteRobotPort"))
        systemProperty("errorScreenShotPath", properties("errorScreenShotPath"))

        systemProperty("junit.jupiter.extensions.autodetection.enabled", true)

        systemProperty("GRADLE_PROJECT", "CodeTester-IntelliJ-IDEA")
        useJUnitPlatform {}
    }
}
