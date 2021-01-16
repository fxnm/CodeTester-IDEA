package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.data.componentAs
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.stepsProcessing.step
import org.assertj.swing.fixture.JTextComponentFixture
import java.time.Duration
import javax.swing.JTextField


fun RemoteRobot.loginDialogWizard(timeout: Duration = Duration.ofSeconds(5), function: LoginDialog.() -> Unit) {
    step("Search of Login Dialog") {
        val dialog = find<LoginDialog>(DialogFixture.byTitle("Login to CodeTester"), timeout)

        dialog.apply(function)

        if (dialog.isShowing) {
            dialog.close()
        }
    }
}

@FixtureName("Login Dialog")
open class LoginDialog(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    DialogFixture(remoteRobot, remoteComponent) {

    fun enterUsername(username: String) {
        textField("Username:").apply {
            // TODO: 29.12.2020 Change in a runJS
            @Suppress("DEPRECATION") execute {
                JTextComponentFixture(robot, componentAs<JTextField>()).setText(username)
            }
        }
    }

    fun enterPassword(password: String) {
        textField("Password:").apply {
            // TODO: 29.12.2020 Change in a runJS
            @Suppress("DEPRECATION") execute {
                JTextComponentFixture(robot, componentAs<JTextField>()).setText(password)
            }
        }
    }
}
