val remoteRobotPort: String by project
val errorScreenShotPath: String by project

dependencies {
    testImplementation(gradleApi())
    testImplementation(group = "com.intellij.remoterobot", name = "remote-robot", version = "0.10.3")
    testImplementation(group = "com.intellij.remoterobot", name = "remote-fixtures", version = "1.1.18")
}

tasks {
    test {
        // don't run gui tests as part of check
        enabled = false
    }

    register<Test>("runUiTest") {
        // we don't want to cache the results of this.
        outputs.upToDateWhen { false }

        systemProperty("robot-server.port", remoteRobotPort)
        systemProperty("junit.jupiter.extensions.autodetection.enabled", true)
        systemProperty("errorScreenShotPath", errorScreenShotPath)

        systemProperty("idea.pass.privacy.policy", true)
        systemProperty("idea.pass.data.sharing", false)

        systemProperty("GRADLE_PROJECT", "CodeTester-IntelliJ-IDEA")
        useJUnitPlatform {}
    }
}
