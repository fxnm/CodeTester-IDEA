package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.waitFor
import java.time.Duration

fun RemoteRobot.codeTesterToolWindow(
    timeout: Duration = Duration.ofSeconds(2),
    function: CodeTesterExplorer.() -> Unit
) {
    step("CodeTester ToolWindow") {
        find<CodeTesterExplorer>(byXpath("//div[@class='CodeTesterToolWindowPanel']"), timeout).apply(function)
    }
}

@FixtureName("CodeTesterToolWindow")
open class CodeTesterExplorer(
    remoteRobot: RemoteRobot,
    remoteComponent: RemoteComponent
) : DialogFixture(remoteRobot, remoteComponent) {

    fun selectCategory(category: String) {
        // TODO: 26.12.2020 NOT Working !
        var a = jList(byXpath("//div[@class='ComboBox']"))
        print(a)
    }

    fun closeToolWindow() {
        button(
            byXpath("//div[@accessiblename='Close Code Tester Window' and @class='ActionButton' and @myaction='Close Code Tester Window (Close the Code Tester tool window)']")
        ).click()
    }

    fun login() {
        button(
            byXpath("//div[@accessiblename='Login' and @class='ActionButton' and @myaction='Login (Login in to the codetester)']")
        ).click()
    }

    fun logout() {
        button(
            byXpath("//div[@accessiblename='Logout' and @class='ActionButton' and @myaction='Login (Login in to the " + "codetester)']"),
            Duration.ofSeconds(1)
        ).click()
    }

    fun run() {
        val button = actionButton(
            byXpath("//div[@accessiblename='Executes the selected Test' and @class='ActionButton' and @myaction='Executes the selected Test (Executes the selected Test on your Code.)']")
        )

        waitFor(Duration.ofSeconds(5), errorMessage = "Run Button is not enabled") {
            button.rightClick()
            button.isEnabled()
        }

        button.click()
    }

    fun cancel() {
        val button = button(
            byXpath("//div[@accessiblename='Stop the Running Test' and @class='ActionButton' and @myaction='Stop the Running Test (Stop the test currently being executed.)']")
        )

        if (!button.isEnabled()) {
            throw IllegalStateException("Cancel Button is not enabled")
        }

        button.click()
    }

    fun displaySuccessfulChecks() {
        button(
            byXpath("//div[@accessiblename='Display Errors' and @class='ActionButton' and @myaction='Display Errors (Display error results)']")
        ).click()
    }

    fun displayFailedChecks() {
        button(
            byXpath("//div[@accessiblename='Display Successes' and @class='ActionButton' and @myaction='Display Successes (Display success results)']")
        ).click()
    }

    fun closeAllOpenResultToolWindows() {
        val button = button(
            byXpath("//div[@accessiblename='Removes all open Tests' and @class='ActionButton' and @myaction='Removes all open Tests (Removes all previously opend Check Result Tool Windows)']")
        )

        if (!button.isEnabled()) {
            throw IllegalStateException("Close all open result tool windows is not enabled")
        }

        button.click()
    }

    fun reload() {
        val button = button(
            byXpath("//div[@accessiblename='Reloads categories' and @class='ActionButton' and @myaction='Reloads categories (Reloads categories)']")
        )

        if (!button.isEnabled()) {
            throw IllegalStateException("Reload is not enabled")
        }

        button.click()
    }

    fun checkIfErrorMessageExists(errorMessage: String): Boolean {
        jLabel(errorMessage)
        return true
    }

    fun findResultTree(): JTreeFixture {
        return find<JTreeFixture>(byXpath("//div[@class='Tree']"), Duration.ofSeconds(10))
    }
}
