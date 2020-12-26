package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.stepsProcessing.step
import java.time.Duration


fun RemoteRobot.loginDialogWizard(
    timeout: Duration = Duration.ofSeconds(5),
    function: LoginDialog.() -> Unit
) {
    step("Search of Login Dialog") {
        val dialog = find<LoginDialog>(DialogFixture.byTitle("Login to CodeTester"), timeout)

        dialog.apply(function)

        if (dialog.isShowing) {
            dialog.close()
        }
    }
}

@FixtureName("Login Dialog")
open class LoginDialog(
    remoteRobot: RemoteRobot,
    remoteComponent: RemoteComponent
) : DialogFixture(remoteRobot, remoteComponent) {

    fun enterUsername(username: String) {
        textField("Username:").text = username
    }

    fun enterPassword(password: String) {
        textField("Password:").text = password
    }
}
