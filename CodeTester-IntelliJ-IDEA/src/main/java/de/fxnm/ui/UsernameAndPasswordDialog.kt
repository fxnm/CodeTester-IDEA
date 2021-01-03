package de.fxnm.ui

import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.Pair
import com.intellij.ui.layout.panel
import de.fxnm.util.CodeTesterBundle
import javax.swing.JComponent
import javax.swing.JPasswordField
import javax.swing.JTextField

/**
 * Class that creates a LoginFrom that contains username and password.
 * This is done by extending the DialogWrapper.
 */
class UsernameAndPasswordDialog(title: String) : DialogWrapper(true) {

    private val userNameField = JTextField()
    private val passwordField = JPasswordField()
    val userInput: Pair<String, String>
        get() = Pair(userNameField.text, String(passwordField.password))

    override fun createCenterPanel(): JComponent {
        return panel {
            row(CodeTesterBundle.message("plugin.ui.usernameAndPassword.username")) {
                component(userNameField)
            }
            row(CodeTesterBundle.message("plugin.ui.usernameAndPassword.password")) {
                component(passwordField)
            }
        }
    }

    init {
        this.init()
        this.title = title
    }
}
