package de.fxnm.ui.account

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.Pair
import com.intellij.ui.layout.panel
import de.fxnm.util.CodeTesterBundle
import javax.swing.JComponent
import javax.swing.JPasswordField
import javax.swing.JTextField

class LoginDialog (title: String) : DialogWrapper(true) {

    private val userNameField = JTextField()
    private val passwordField = JPasswordField()
    val userInput: Pair<String, String>
        get() = Pair(userNameField.text, String(passwordField.password))

    override fun createCenterPanel(): JComponent {
        return panel {
            row(CodeTesterBundle.message("plugin.runner.LoginRunner.UserName")) {
                component(userNameField)
            }
            row(CodeTesterBundle.message("plugin.runner.LoginRunner.Password")) {
                component(passwordField)
            }
        }
    }

    init {
        this.init()
        this.title = title
    }
}
