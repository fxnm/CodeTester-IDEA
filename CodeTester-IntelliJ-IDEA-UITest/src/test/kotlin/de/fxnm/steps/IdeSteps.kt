package de.fxnm.steps

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.actionMenu
import de.fxnm.fixtures.actionMenuItem
import de.fxnm.fixtures.idea

class IdeSteps {
    companion object {
        fun closeCurrentOpenProject() {
            uiTest {
                idea {
                    step("Close the current open project") {
                        actionMenu("File").click()
                        actionMenuItem("Close Project").click()
                    }
                }
            }
        }
    }
}