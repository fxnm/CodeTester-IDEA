package de.fxnm.steps

import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.waitFor
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.codeTesterToolWindow
import de.fxnm.fixtures.idea
import de.fxnm.fixtures.loginDialogWizard

class CodeTesterToolWindowSteps {
    companion object {
        fun loginWithCredentials(username: String, password: String) {
            step("Run Login With Credentials Mehtode") {
                uiTest {
                    idea {
                        showCodeTesterExplorer()
                        codeTesterToolWindow {
                            step("Open login dialog") {
                                login()
                            }

                            step("Fill login information") {
                                loginDialogWizard {
                                    enterUsername(username)
                                    enterPassword(password)
                                    pressOk()
                                }
                            }

                            waitFor {
                                checkIfErrorMessageExists("Reload Successful")
                            }

                            closeToolWindow()
                        }
                    }
                }
            }
        }


        fun logout() {
            uiTest {
                idea {
                    step("Logout from CodeTester") {
                        showCodeTesterExplorer()
                        codeTesterToolWindow {
                            try {
                                logout()
                            } catch (e: Exception) {
                            }
                        }
                    }
                }
            }
        }
    }
}