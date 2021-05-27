package de.fxnm.steps

import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.*

class CodeTesterToolWindowSteps {
    companion object {
        fun loginWithCredentials(username: String, password: String) {
            step("Run Login With Credentials Mehtode") {
                uiTest {
                    idea {
                        showCodeTesterExplorer()
                        codeTesterToolWindow {
                            step("Open login dialog") {
                                loginButton {
                                   click()
                                }
                            }

                            step("Fill login information") {
                                loginDialogWizard {
                                    enterUsername(username)
                                    enterPassword(password)
                                    pressOk()
                                }
                            }

                            // Wait till user is logged in
                            waitForBackgroundTasks()

                            // Checks if the category reload is finished
                            val combobox = comboBox(byXpath("//div[@class='ComboBox']"))
                            if (combobox.listValues().isEmpty()) {
                                waitForBackgroundTasks()
                            }
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

                        codeTesterTab {
                            selectMainPanel()
                        }

                        codeTesterToolWindow {
                            try {
                                logoutButton {
                                    clickWhenEnabled()
                                }
                            } catch (ignore: Exception) {
                            }
                        }
                    }
                }
            }
        }
    }
}
