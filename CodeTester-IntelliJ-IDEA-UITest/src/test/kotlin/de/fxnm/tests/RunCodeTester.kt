package de.fxnm.tests

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.codeTesterToolWindow
import de.fxnm.fixtures.idea
import de.fxnm.steps.CodeTesterToolWindowSteps
import de.fxnm.util.EnvironmentVariable
import org.junit.jupiter.api.Test

class RunCodeTester : UiTestcaseBase() {

    @Test
    fun runCodeTester() {
        CodeTesterToolWindowSteps.loginWithCredentials(EnvironmentVariable.get(EnvironmentVariable.USERNAME),
                                                       EnvironmentVariable.get(EnvironmentVariable.PASSWORD))

        uiTest {
            idea {
                showCodeTesterExplorer()
                codeTesterToolWindow {
                    step("Run Test") {
                        selectCategory("2020 SS Final 2")
                        run()

                        // TODO: 26.12.2020
                        waitForBackgroundTasks()
                    }
                }
            }
        }
    }
}
