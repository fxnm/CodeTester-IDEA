package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.ContainerFixture
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.fixtures.JTextFieldFixture
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.waitForIgnoringError
import java.io.File
import java.nio.file.Path
import java.time.Duration

fun ContainerFixture.fileBrowser(partialTitle: String,
                                 timeout: Duration = Duration.ofSeconds(20),
                                 function: FileBrowserFixture.() -> Unit = {}) {
    step("Search for file explorer with title matching $partialTitle") {
        val dialog = find<FileBrowserFixture>(DialogFixture.byTitleContains(partialTitle), timeout)

        dialog.apply(function)

        if (dialog.isShowing) {
            dialog.close()
        }

        dialog
    }
}

@FixtureName("FileBrowser")
class FileBrowserFixture(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    DialogFixture(remoteRobot, remoteComponent) {
    private val tree by lazy {
        find<JTreeFixture>(byXpath("//div[@class='Tree']"), Duration.ofSeconds(10)).also {
            it.separator = "|" // switch out the separator since tree has path "/" as the root
        }
    }

    fun selectFile(path: Path) {
        val absolutePath = path.toAbsolutePath()
        step("Select $absolutePath") {
            step("Refresh file explorer to make sure the file ${path.fileName} is loaded") {
                waitForIgnoringError(duration = Duration.ofSeconds(30), interval = Duration.ofSeconds(10)) {
                    setFilePath(absolutePath)
                    findAndClick("//div[@accessiblename='Refresh']")
                    tree.requireSelection(*absolutePath.toParts())
                    true
                }
            }

            pressOk()
        }
    }

    private fun setFilePath(path: Path) {
        step("Set file path to $path") {
            val pathBox: JTextFieldFixture = find(byXpath("//div[@class='BorderlessTextField']"), Duration.ofSeconds(5))
            pathBox.text = path.toString()
        }
    }

    private fun Path.toParts(): Array<String> {
        val parts = this.toString().split(File.separatorChar).toMutableList()
        if (this.toString().startsWith(File.separatorChar)) {
            parts[0] = File.separator
        }
        return parts.toTypedArray()
    }
}
