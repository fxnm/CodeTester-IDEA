package de.fxnm.tests

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.codeTesterToolWindow
import de.fxnm.fixtures.idea
import de.fxnm.fixtures.loginDialogWizard
import org.junit.jupiter.api.Test


class FirstTryTesting : UiTestcaseBase() {


    @Test
    fun testInsightsQuery() {
        uiTest {
            idea {
                showCodeTesterExplorer()
                codeTesterToolWindow {
                    step("Open login dialog") {
                        login()
                    }

                    step("Fill login information") {
                        loginDialogWizard {
                            enterUsername("username")
                            enterPassword("password")
                            pressOk()
                        }
                    }

                    closeToolWindow()
                }
            }
        }
    }
}
