package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.fixtures.DefaultXpath
import com.intellij.remoterobot.search.locators.byXpath
import java.time.Duration
import kotlin.streams.toList

fun ContainerFixture.checkResultTree(function: ResultTreeFixture.() -> Unit = {}) {
    val fixture = find(ResultTreeFixture::class.java)
    fixture.apply(function)
}


@DefaultXpath("type", "//div[@class='Tree']")
class ResultTreeFixture(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    CommonContainerFixture(remoteRobot, remoteComponent) {

    private val tree by lazy {
        find<JTreeFixture>(byXpath("//div[@class='Tree']"), Duration.ofSeconds(10))
    }

    fun getVisibleItems(): List<String> {
        return tree.retrieveData().textDataList.stream().map { s -> s.text }.toList()
    }

    fun doubleClickItem(itemNumber: Int) {
        findText(tree.extractData()[itemNumber].text).doubleClick()
    }

    fun doubleClickItem(itemName:String) {
        findText(itemName).doubleClick()
    }
}
