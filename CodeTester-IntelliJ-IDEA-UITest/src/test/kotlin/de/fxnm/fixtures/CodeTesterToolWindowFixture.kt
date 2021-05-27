package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.ActionButtonFixture
import com.intellij.remoterobot.fixtures.ComboBoxFixture
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.DefaultXpath
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step

fun RemoteRobot.codeTesterToolWindow(function: CodeTesterExplorer.() -> Unit) {
    step("CodeTester ToolWindow") {
        find<CodeTesterExplorer>(CodeTesterExplorer::class.java).apply(function)
    }
}

@DefaultXpath("type", "//div[@class='CodeTesterToolWindowPanel']")
open class CodeTesterExplorer(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    CommonContainerFixture(remoteRobot, remoteComponent) {

    private val categoryComboBoxXpath = "//div[@class='ComboBox']"
    private val closeToolWindowButtonXpath =
        "//div[@accessiblename='Close Code Tester Window' and @class='ActionButton' and @myaction='Close Code Tester Window (Close the Code Tester tool window)']"
    private val loginButtonXpath =
        "//div[@accessiblename='Login' and @class='ActionButton' and @myaction='Login (Login in to the codetester)']"
    private val logoutButtonXpath =
        "//div[@accessiblename='Logout' and @class='ActionButton' and @myaction='Login (Login in to the codetester)']"
    private val runButtonXpath =
        "//div[@accessiblename='Executes the selected Test' and @class='ActionButton' and @myaction='Executes the selected Test (Executes the selected Test on your Code.)']"
    private val cancelButtonXpath =
        "//div[@accessiblename='Stop the Running Test' and @class='ActionButton' and @myaction='Stop the Running Test (Stop the test currently being executed.)']"
    private val displayFailedChecksButtonXpath =
        "//div[@accessiblename='Display Errors' and @class='ActionButton' and @myaction='Display Errors (Display error results)']"
    private val displaySuccessfulChecksButtonXpath =
        "//div[@accessiblename='Display Successes' and @class='ActionButton' and @myaction='Display Successes (Display success results)']"
    private val addCheckButtonXpath =
        "//div[@accessiblename='Add Test' and @class='ActionButton' and @myaction='Add Test (Add a Test to the public test set)']"
    private val closeAllToolWindowsButtonXpath =
        "//div[@accessiblename='Removes all open Tests' and @class='ActionButton' and @myaction='Removes all open Tests (Removes all previously opend Check Result Tool Windows)']"
    private val reloadCategoriesButtonXpath =
        "//div[@accessiblename='Reloads categories' and @class='ActionButton' and @myaction='Reloads categories (Reloads categories)']"


    fun categoryComboBox(function: ComboBoxFixture.() -> Unit) {
        comboBox(byXpath(categoryComboBoxXpath)).also(function)
    }

    fun closeToolWindowButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(closeToolWindowButtonXpath)).apply(function)
    }

    fun loginButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(loginButtonXpath)).apply(function)
    }

    fun logoutButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(logoutButtonXpath)).apply(function)
    }

    fun runButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(runButtonXpath)).apply(function)
    }

    fun cancelButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(cancelButtonXpath)).apply(function)
    }

    fun displaySuccessfulButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(displaySuccessfulChecksButtonXpath)).apply(function)
    }

    fun displayFailedChecksButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(displayFailedChecksButtonXpath)).apply(function)
    }

    fun addCheckButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(addCheckButtonXpath)).apply(function)
    }

    fun closeOpenToolWindowsButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(closeAllToolWindowsButtonXpath)).apply(function)
    }

    fun reloadButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(reloadCategoriesButtonXpath)).apply(function)
    }
}
