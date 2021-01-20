package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.ActionButtonFixture
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.fixtures.DefaultXpath
import com.intellij.remoterobot.search.locators.byXpath

fun ContainerFixture.checkResultToolWindow(checkName: String, function: CheckResultToolWindowFixture.() -> Unit = {}) {
    val fixtureList = findAll(CheckResultToolWindowFixture::class.java)
    for (fixture in fixtureList) {
        if (fixture.findAllText(checkName).isNotEmpty()) {
            fixture.apply(function)
            return
        }
    }

    throw RuntimeException("Did not find check Result with name: $checkName")
}

@DefaultXpath("type", "//div[@class='MyNonOpaquePanel']")
class CheckResultToolWindowFixture(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    CommonContainerFixture(remoteRobot, remoteComponent) {

    private val closeToolWindowButtonXpath =
        "//div[@accessiblename='Close Code Tester Window' and @class='ActionButton' and @myaction='Close Code Tester Window (Close the Code Tester tool window)']"
    private val gotToHomeScreenButtonXpath =
        "//div[@accessiblename='To Homescreen' and @class='ActionButton' and @myaction='To Homescreen (Jumps to the check result summary)']"
    private val rerunChecksButtonXpath =
        "//div[@accessiblename='Reruns the selected Test' and @class='ActionButton' and @myaction='Reruns the selected Test (Reruns the selected Test on your Code.)']"
    private val cancelRunChecksButtonXpath =
        "//div[@accessiblename='Stop the Running Test' and @class='ActionButton' and @myaction='Stop the Running Test (Stop the test currently being executed.)']"

    private val titleComponent by lazy {
        val fixtureList = findAll<CommonContainerFixture>(byXpath("//div[@class='Box']"))
        for (fixture in fixtureList) {
            if (fixture.findAll<CommonContainerFixture>(byXpath("//div[@class='ActionToolbarImpl']")).isEmpty()) {
                return@lazy fixture
            }
        }
        throw RuntimeException("Failed to find title component")
    }

    fun getCheckName(): String {
        return titleComponent.extractData()[0].text
    }

    fun getCheckStatus(): String {
        return titleComponent.extractData()[1].text
    }

    fun getErrorMessage(): String {
        // TODO: 19.01.2021
        return ""
    }

    fun closeToolWindowButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(closeToolWindowButtonXpath)).apply(function)
    }

    fun gotToHomeScreenButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(gotToHomeScreenButtonXpath)).apply(function)
    }

    fun rerunChecksButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(rerunChecksButtonXpath)).apply(function)
    }

    fun cancelCheckRerunButton(function: ActionButtonFixture.() -> Unit) {
        actionButton(byXpath(cancelRunChecksButtonXpath)).apply(function)
    }
}

