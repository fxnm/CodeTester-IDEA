package de.fxnm.ui.settings

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.layout.panel
import de.fxnm.config.settings.global.GlobalSettingsService
import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsService
import de.fxnm.util.CodeTesterBundle
import java.awt.Dimension
import java.util.LinkedList
import javax.swing.JComponent
import javax.swing.JPanel


class SettingsMenuPanel(val project: Project) {

    var currentLoggedInAccount: String = CodeTesterBundle.message("plugin.ui.settingsMenuPanel.accountSettings.currentUser.none")
    private var codeTesterBaseURLs: MutableList<String> = LinkedList()
    private val currentSelectedCodeTesterBaseUrl: String
    var field: ComboBox<String> = ComboBox()

    init {
        codeTesterBaseURLs.addAll(GlobalSettingsService.getService().state?.codeTesterBaseURL!!)
        currentSelectedCodeTesterBaseUrl =
            ProjectPersistentSettingsService.getService(project).state.selectedCodeTesterBaseURL
        if (!codeTesterBaseURLs.contains(currentSelectedCodeTesterBaseUrl)) {
            codeTesterBaseURLs.add(currentSelectedCodeTesterBaseUrl)
        }
        for (strings in codeTesterBaseURLs) {
            field.addItem(strings)
        }
    }


    fun creatUI(): JPanel {
        return panel {
            titledRow(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.accountSettings.title")) {
                row {
                    label(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.accountSettings.currentUser.title"))
                    label(currentLoggedInAccount)
                }
                row {
                    label(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.accountSettings.noAccount"))
                    browserLink(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.accountSettings.signUp"),
                        ProjectPersistentSettingsService.getService(project).state.selectedCodeTesterBaseURL)
                }
            }

            titledRow(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.serverSettings.title")) {
                row(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.serverSettings.currentServer.title")) {
                    field
                    field.selectedItem = currentSelectedCodeTesterBaseUrl
                    field(growX)
                }
            }

            titledRow(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.helpSettings.title")) {
                row {
                    browserLink(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.helpSettings.bugReport"),
                            "https://github.com/fxnm/CodeTester-IDEA/issues/new/choose")
                }
                row {
                    browserLink(CodeTesterBundle.message("plugin.ui.settingsMenuPanel.helpSettings.sourceCode"),
                            "https://github.com/fxnm/CodeTester-IDEA")
                }
            }
        }
    }
}
