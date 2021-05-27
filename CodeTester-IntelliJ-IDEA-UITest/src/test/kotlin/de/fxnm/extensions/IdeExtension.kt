package de.fxnm.extensions

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.stepsProcessing.StepLogger
import com.intellij.remoterobot.stepsProcessing.StepWorker
import com.intellij.remoterobot.stepsProcessing.log
import com.intellij.remoterobot.utils.waitFor
import de.fxnm.steps.IdeSteps
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.io.IOException
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

private val initialSetup = AtomicBoolean(false)
private val robotPort = System.getProperty("robot-server.port")!!.toInt()
private val errorScreenShotPath = System.getProperty("errorScreenShotPath")!!

fun uiTest(test: RemoteRobot.() -> Unit) {
    if (!initialSetup.getAndSet(true)) {
        StepWorker.registerProcessor(StepLogger())
    }

    RemoteRobot("http://127.0.0.1:$robotPort").apply(test)
}

class Ide : BeforeAllCallback, AfterAllCallback, AfterTestExecutionCallback {

    override fun beforeAll(context: ExtensionContext) {
        log.info("Gradle process started, trying to connect to IDE")
        waitForIde()
        log.info("Connected to IDE")
    }

    private fun waitForIde() {
        waitFor(
            duration = Duration.ofMinutes(10),
            interval = Duration.ofMillis(500),
            errorMessage = "Could not connect to remote robot in time"
        ) {

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
