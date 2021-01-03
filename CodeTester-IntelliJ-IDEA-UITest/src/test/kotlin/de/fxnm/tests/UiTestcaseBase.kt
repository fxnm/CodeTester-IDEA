package de.fxnm.tests

import de.fxnm.steps.CodeTesterToolWindowSteps
import de.fxnm.steps.IdeSteps
import de.fxnm.steps.WelcomeFrameSteps
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path

open class UiTestcaseBase {

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        WelcomeFrameSteps.openNewProject("11", true, "Test Project", tempDir)
    }

    @AfterEach
    fun closeProject() {
        CodeTesterToolWindowSteps.logout()
        IdeSteps.closeCurrentOpenProject()
    }
}
