package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.fixtures.DefaultXpath
import com.intellij.remoterobot.search.locators.byXpath

fun ContainerFixture.codeTesterTab(function: ToolWindowHeader.() -> Unit = {}) {
    val xpathList = findAll(ToolWindowHeader::class.java)
    for (xpath in xpathList) {
        if (xpath.isCodeTesterTabPanel()) {
            xpath.apply(function)
        }
    }
}

@DefaultXpath("type", "//div[@accessiblename='Tool Window Header' and @class='ToolWindowHeader']")
class ToolWindowHeader(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    CommonContainerFixture(remoteRobot, remoteComponent) {

    private val codeTesterTabLabelXpath =
        "//div[@accessiblename='CodeTester:' and @class='BaseLabel' and @text='CodeTester:']"

    private val codeTesterBaseLabelXpath =
        "//div[@accessiblename='Code Tester' and @class='ContentTabLabel' and @text='Code Tester']"

    fun isCodeTesterTabPanel(): Boolean {
        return findAll<DialogFixture>(byXpath(codeTesterTabLabelXpath)).isNotEmpty()
    }

    fun selectMainPanel() {
        find<DialogFixture>(byXpath(codeTesterBaseLabelXpath)).click()
    }


    fun selectPanel(panelName: String) {

        // Ugly, cause Names can get shortened.

        val name = panelName.replace("Check '", "")
        val fixtureList = findAll<DialogFixture>(byXpath("//div[@class='ContentTabLabel']"))
        for (fixture in fixtureList) {
            if (name.startsWith(fixture.extractData()[0].text.replace("...", ""))) {
                fixture.click()
                return
            }
        }

        throw RuntimeException("Could not found a fixture starting with: $name")
    }
}
