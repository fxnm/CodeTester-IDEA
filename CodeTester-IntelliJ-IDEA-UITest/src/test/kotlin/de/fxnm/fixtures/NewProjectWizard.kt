package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import java.time.Duration

fun RemoteRobot.newProjectWizard(timeout: Duration = Duration.ofSeconds(20),
                                 function: NewProjectWizardDialog.() -> Unit) {
    step("Search for new project wizard dialog") {
        val dialog = find<NewProjectWizardDialog>(DialogFixture.byTitle("New Project"), timeout)

        dialog.apply(function)

        if (dialog.isShowing) {
            dialog.close()
        }
    }
}

@FixtureName("New Project Wizard")
open class NewProjectWizardDialog(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    DialogFixture(remoteRobot, remoteComponent) {


    fun selectProjectCategory(type: String) {
        findText(type).click()
    }

    fun selectProjectSDK(sdkVersion: String) {
        button(byXpath("//div[@class='JdkComboBox']")).click()
        jList(byXpath("//div[@class='MyList']")).findText(sdkVersion).click()
    }

    fun createProjectFromTemplate() {
        checkBox("Create project from template").click()
    }

    fun setProjectName(projectName: String) {
        textField("Project Name:").text = projectName
    }

    fun setProjectLocation(folder: String) {
        textField("Project location:").text = folder
    }

    fun pressNext() {
        button("Next").click()
    }

    fun pressFinish() {
        button("Finish").click()
    }
}
