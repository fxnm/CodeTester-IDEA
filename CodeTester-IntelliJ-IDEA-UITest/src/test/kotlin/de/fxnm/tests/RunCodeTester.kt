package de.fxnm.tests

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.checkResultTree
import de.fxnm.fixtures.codeTesterTab
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

                        waitForBackgroundTasks()

                        checkResultTree {
                            doubleClickItem("Check 'Feiertage-Test (Fehlerhaftes Format der Datei)' : FAILED")
                        }
                    }
                }

                codeTesterTab {
                    selectMainPanel()
                    selectPanel("Check 'Feiertage-Test (Fehlerhaftes Format der Datei)' : FAILED")
                }
            }
        }
    }
}
