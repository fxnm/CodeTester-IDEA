val remoteRobotPort: String by project

dependencies {
    testImplementation(gradleApi())
    testImplementation(group = "com.intellij.remoterobot", name = "remote-robot", version = "0.10.0")
    testImplementation(group = "com.intellij.remoterobot", name = "remote-fixtures", version = "1.1.18")
}

tasks {
    test {
        // don't run gui tests as part of check
        enabled = false
    }

    register<Test>("uiTest") {
        // we don't want to cache the results of this.
        outputs.upToDateWhen { false }

        systemProperty("robot-server.port", remoteRobotPort)


        useJUnitPlatform()
    }
}