package de.fxnm.tests

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.codeTesterToolWindow
import de.fxnm.fixtures.idea
import de.fxnm.steps.CodeTesterToolWindowSteps
import de.fxnm.util.EnvironmentVariable
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CodeTesterToolWindowButton : UiTestcaseBase() {

    @Test
    fun loggedOutState() {
        uiTest {
            idea {
                showCodeTesterExplorer()

                codeTesterToolWindow {
                    step("Check CloseToolWindow Button") {
                        closeToolWindowButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Check Login Button") {
                        loginButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Check Run Button") {
                        runButton {
                            Assertions.assertFalse(isEnabled())
                        }
                    }

                    step("Cancel Check Run Button") {
                        cancelButton {
                            Assertions.assertFalse(isEnabled())
                        }
                    }

                    step("Display Error Checks Button") {
                        displayFailedChecksButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Display Successful Checks Button") {
                        displaySuccessfulButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Add new Check Button") {
                        addCheckButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Close all open tabs Button") {
                        closeOpenToolWindowsButton {
                            Assertions.assertFalse(isEnabled())
                        }
                    }

                    step("Reload Category Button") {
                        reloadButton {
                            Assertions.assertFalse(isEnabled())
                        }
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
                    step("Check CloseToolWindow Button") {
                        closeToolWindowButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Check Login Button") {
                        logoutButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Check Run Button") {
                        runButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Cancel Check Run Button") {
                        cancelButton {
                            Assertions.assertFalse(isEnabled())
                        }
                    }

                    step("Display Error Checks Button") {
                        displayFailedChecksButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Display Successful Checks Button") {
                        displaySuccessfulButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Add new Check Button") {
                        addCheckButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }

                    step("Close all open tabs Button") {
                        closeOpenToolWindowsButton {
                            Assertions.assertFalse(isEnabled())
                        }
                    }

                    step("Reload Category Button") {
                        reloadButton {
                            Assertions.assertTrue(isEnabled())
                        }
                    }
                }
            }
        }
    }
}
