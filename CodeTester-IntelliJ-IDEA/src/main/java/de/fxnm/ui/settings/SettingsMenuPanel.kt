package de.fxnm.ui.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.layout.panel
import de.fxnm.config.settings.global.GlobalSettingsService
import de.fxnm.config.settings.project.ProjectSettingsService
import java.util.LinkedList
import javax.swing.JPanel


class SettingsMenuPanel(project: Project) {

    var currentLoggedInAccount: String = "Not Logged In"
    private var codeTesterBaseURLs: MutableList<String> = LinkedList()
    private val currentSelectedCodeTesterBaseUrl: String
    var field: ComboBox<String> = ComboBox()

    init {
        codeTesterBaseURLs.addAll(GlobalSettingsService.getService().state?.codeTesterBaseURL!!)
        currentSelectedCodeTesterBaseUrl = ProjectSettingsService.getService(project).state.selectedCodeTesterBaseURL
        if (!codeTesterBaseURLs.contains(currentSelectedCodeTesterBaseUrl)) {
            codeTesterBaseURLs.add(currentSelectedCodeTesterBaseUrl)
        }
        for (strings in codeTesterBaseURLs) {
            field.addItem(strings)
        }
    }


    fun creatUI(): JPanel {
        return panel {
            titledRow("Account Settings") {
                row {
                    label("Logged in Account:")
                    label(currentLoggedInAccount)
                }
                row {
                    label("Do not have an account?")
                    browserLink("Sign Up", "https://codetester.ialistannen.de")
                }
            }

            titledRow("Code Tester Server Settings") {
                row("Current selected Server:") {
                    field
                    field.selectedItem = currentSelectedCodeTesterBaseUrl
                    field(growX)
                }
            }

            titledRow("Need Help?") {
                row {
                    browserLink("Bug Report and Feature Request",
                            "https://github.com/fxnm/CodeTester-IDEA/issues/new/choose")
                }
                row {
                    browserLink("Source Code",
                            "https://github.com/fxnm/CodeTester-IDEA")
                }
            }
        }
    }
}
