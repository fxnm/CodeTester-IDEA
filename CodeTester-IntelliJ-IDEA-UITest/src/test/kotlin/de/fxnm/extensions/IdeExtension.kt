package de.fxnm.extensions

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.stepsProcessing.StepLogger
import com.intellij.remoterobot.stepsProcessing.StepWorker
import com.intellij.remoterobot.stepsProcessing.log
import com.intellij.remoterobot.utils.waitFor
import de.fxnm.steps.IdeSteps
import org.gradle.tooling.*
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.IOException
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

private val initialSetup = AtomicBoolean(false)
private val robotPort = System.getProperty("robot-server.port").toInt()
private val errorScreenShotPath = System.getProperty("errorScreenShotPath")

fun uiTest(test: RemoteRobot.() -> Unit) {
    if (!initialSetup.getAndSet(true)) {
        StepWorker.registerProcessor(StepLogger())
    }

    RemoteRobot("http://127.0.0.1:$robotPort").apply(test)
}

class Ide : BeforeAllCallback, AfterAllCallback, AfterTestExecutionCallback {
    private val gradleProject = System.getProperty("GRADLE_PROJECT")
    private val gradleProcess = GradleProcess()

    override fun beforeAll(context: ExtensionContext) {
        gradleProcess.startGradleTasks(":$gradleProject:runIdeForUiTests")
        log.info("Gradle process started, trying to connect to IDE")
        waitForIde()
        log.info("Connected to IDE")
    }

    private fun waitForIde() {
        waitFor(duration = Duration.ofMinutes(10),
                interval = Duration.ofMillis(500),
                errorMessage = "Could not connect to remote robot in time") {
            if (!gradleProcess.isRunning()) {
                throw IllegalStateException("Gradle task has ended, check log")
            }

            canConnectToToRobot()
        }
    }

    private fun canConnectToToRobot(): Boolean = try {
        Socket().use { socket ->
            socket.connect(InetSocketAddress("127.0.0.1", robotPort))
            true
        }
    } catch (e: IOException) {
        false
    }

    override fun afterTestExecution(context: ExtensionContext) {
        val testMethod = context.requiredTestMethod
        val testFailed = context.executionException.isPresent
        if (testFailed) {
            IdeSteps.takeScreenShot("failed_test_${testMethod.name}", errorScreenShotPath)
        }
    }

    override fun afterAll(context: ExtensionContext) {
        log.info("Stopping Gradle process")
        gradleProcess.stopGradleTask()
    }
}

private class GradleProcess {
    private val gradleConnection: ProjectConnection
    private var cancellationTokenSource: CancellationTokenSource? = null
    private val isRunning = AtomicBoolean(false)

    init {
        val cwd = Paths.get(".").toAbsolutePath()
        if (!Files.exists(cwd.resolve("build.gradle.kts"))) {
            throw IllegalStateException("Failed to locate build.gradle.kts in $cwd}")
        }

        gradleConnection = GradleConnector.newConnector().forProjectDirectory(cwd.toFile()).connect()
    }

    fun isRunning(): Boolean = isRunning.get()

    fun startGradleTasks(vararg gradleTask: String) {
        val tokenSource = GradleConnector.newCancellationTokenSource()

        gradleConnection.newBuild().forTasks(*gradleTask).withCancellationToken(tokenSource.token())
            .setColorOutput(false).setStandardOutput(OutputWrapper(true)).setStandardError(OutputWrapper(false))
            .run(object : ResultHandler<Any> {
                override fun onFailure(failure: GradleConnectionException) {
                    isRunning.set(false)
                }

                override fun onComplete(result: Any) {
                    isRunning.set(false)
                }
            })

        isRunning.set(true)

        this.cancellationTokenSource = tokenSource
    }

    fun stopGradleTask() {
        cancellationTokenSource?.let {
            it.cancel()

            cancellationTokenSource = null
        }
        waitFor(duration = Duration.ofMinutes(1),
                interval = Duration.ofMillis(500),
                errorMessage = "Could not stop Gradle task") {
            !isRunning.get()
        }
        log.info("Gradle process stopped")
    }
}

class OutputWrapper(private val isStdOut: Boolean) : OutputStream() {
    private var buffer = StringBuilder()

    override fun write(b: Int) {
        val c = b.toChar()
        buffer.append(c)
        if (c == '\n') {
            doFlush()
        }
    }

    private fun doFlush() {
        val message = "[IDE Gradle]: ${buffer.trim()}"
        if (isStdOut) {
            log.info(message)
        } else {
            log.error(message)
        }
        buffer.clear()
    }
}
