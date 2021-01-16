package de.fxnm.steps

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.idea
import de.fxnm.fixtures.newProjectWizard
import de.fxnm.fixtures.welcomeFrame
import java.nio.file.Path

class WelcomeFrameSteps {

    companion object {
        fun openNewProject(
            javaVersion: String,
            withTemplate: Boolean,
            projectName: String,
            projectLocation: Path,
        ) {
            uiTest {
                welcomeFrame {
                    step("Open Project Wizard") {
                        openNewProjectWizard()
                    }

                    step("Crete New Application") {
                        newProjectWizard {
                            step("Select Java $javaVersion Application") {
                                selectProjectCategory("Java")
// Todo Not working properly if SDK Version is not available
//                                selectProjectSDK(javaVersion)
                                pressNext()
                            }

                            if (withTemplate) {
                                step("Create Project from Template") {
                                    createProjectFromTemplate()
                                }
                            }
                            pressNext()

                            step("Set Name and Location") {
                                setProjectName(projectName)
                                setProjectLocation(projectLocation.toAbsolutePath().toString())
                            }

                            step("Create") {
                                pressFinish()
                            }
                        }
                    }
                }
            }

            waitTillProjectIsOpen()
        }

        fun openProjectFromPath(projectPath: Path) {
            uiTest {
                step("Open Project from Path") {
                    welcomeFrame {
                        openFolder(projectPath)
                    }
                }
            }
            waitTillProjectIsOpen()
        }

        private fun waitTillProjectIsOpen() {
            uiTest {
                idea {
                    step("Wait and clean") {
                        waitForBackgroundTasks()
                        dumbAware {
                            tryCloseTips()
                        }
                    }
                }
            }
        }
    }
}
