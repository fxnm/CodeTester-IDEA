package de.fxnm.tests

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.*
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
                        categoryComboBox {
                            selectItem("2020 SS Final 2")
                        }

                        runButton {
                            clickWhenEnabled()
                        }

                        waitForBackgroundTasks()

                        checkResultTree {
                            doubleClickItem("Check 'Feiertage-Test (Fehlerhaftes Format der Datei)' : FAILED")
                        }
                    }
                }

                checkResultToolWindow("Feiertage-Test (Fehlerhaftes Format der Datei)") {
                    rerunChecksButton {
                        clickWhenEnabled()
                    }

                    waitForBackgroundTasks()
                }


                codeTesterTab {
                    selectMainPanel()
                    selectPanel("Check 'Feiertage-Test (Fehlerhaftes Format der Datei)' : FAILED")
                    selectMainPanel()
                }
            }
        }
    }
}
