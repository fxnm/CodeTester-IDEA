package de.fxnm.tests

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.*
import de.fxnm.steps.CodeTesterToolWindowSteps
import de.fxnm.util.EnvironmentVariable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ResultToolWindowButton : UiTestcaseBase() {


    @Test
    fun loggedOutState() {
        CodeTesterToolWindowSteps.loginWithCredentials(EnvironmentVariable.get(EnvironmentVariable.USERNAME),
                                                       EnvironmentVariable.get(EnvironmentVariable.PASSWORD))

        uiTest {
            idea {
                showCodeTesterExplorer()
                codeTesterToolWindow {
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


                codeTesterTab {
                    selectMainPanel()
                }

                codeTesterToolWindow {
                    logoutButton {
                        clickWhenEnabled()
                    }
                }


                codeTesterTab {
                    //Tab should not exist after logout
                    Assertions.assertThrows(RuntimeException::class.java) {
                        selectPanel("Check 'Feiertage-Test (Fehlerhaftes Format der Datei)' : FAILED")
                    }
                }
            }
        }
    }

    @Test
    fun loggedInState() {
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
                    step("Check CloseToolWindow Button") {
                        closeToolWindowButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Go to Home Screen Button") {
                        gotToHomeScreenButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Rerun Checks Button") {
                        rerunChecksButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Cancel Check Run Button") {
                        cancelCheckRerunButton {
                            Assertions.assertFalse(isEnabled())
                        }
                    }
                }

                codeTesterTab {
                    selectMainPanel()
                }
            }
        }
    }
}
