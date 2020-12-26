package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.ComponentFixture
import com.intellij.remoterobot.fixtures.DefaultXpath
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.log
import com.intellij.remoterobot.stepsProcessing.step
import java.nio.file.Path
import java.time.Duration

fun RemoteRobot.welcomeFrame(function: WelcomeFrame.() -> Unit) {
    find(WelcomeFrame::class.java, Duration.ofSeconds(10)).apply(function)
}

@FixtureName("Welcome Frame")
@DefaultXpath("type", "//div[@class='FlatWelcomeFrame' and @visible='true']")
class WelcomeFrame(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    CommonContainerFixture(remoteRobot, remoteComponent) {
    fun openNewProjectWizard() {
        selectTab("Projects")
        // This can match two things: If no previous projects, its a SVG icon, else a jbutton
        findAll<ComponentFixture>(byXpath("//div[contains(@accessiblename, 'New Project') and (@class='MainButton' or @class='JButton')]")).first()
            .click()
    }

    fun openPreferences() = step("Opening preferences dialog") {

        selectTab("Customize")
        findAndClick("//div[@accessiblename='All settings…']")

        log.info("Successfully opened the preferences dialog")
    }

    private fun selectTab(tabName: String) {
        jList(byXpath("//div[@accessiblename='Welcome screen categories']")).selectItem(tabName)
    }

    fun openFolder(path: Path) {
        selectTab("Projects")
        findAll<ComponentFixture>(byXpath("//div[contains(@accessiblename, 'Open') and (@class='MainButton' or @class='JButton')]")).first()
            .click()
        fileBrowser("Open") {
            selectFile(path)
        }
    }
}
