package de.fxnm.fixtures

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.*
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.waitFor
import java.awt.image.BufferedImage
import java.time.Duration
import javax.imageio.ImageIO

fun RemoteRobot.idea(function: IdeaFrame.() -> Unit) {
    val frame = find<IdeaFrame>(timeout = Duration.ofSeconds(10))
    frame.apply(function)
}

@FixtureName("Idea frame")
@DefaultXpath("IdeFrameImpl type", "//div[@class='IdeFrameImpl']")
class IdeaFrame(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    CommonContainerFixture(remoteRobot, remoteComponent) {
    val projectViewTree
        get() = find<ContainerFixture>(byXpath("ProjectViewTree", "//div[@class='ProjectViewTree']"))

    init {
        waitForProjectToBeAssigned()
    }

    private fun waitForProjectToBeAssigned() {
        waitFor(duration = Duration.ofSeconds(30)) {
            callJs("""
                var frameHelper = com.intellij.openapi.wm.impl.ProjectFrameHelper.getFrameHelper(component);
                frameHelper.project != null
                """.trimIndent(), runInEdt = true)
        }
    }

    fun dumbAware(timeout: Duration = Duration.ofMinutes(5), function: () -> Unit) {
        step("Wait for smart mode") {
            waitFor(duration = timeout, interval = Duration.ofSeconds(5)) {
                isDumbMode().not()
            }
            function()
            step("..wait for smart mode again") {
                waitFor(duration = timeout, interval = Duration.ofSeconds(5)) {
                    isDumbMode().not()
                }
            }
        }
    }

    fun waitForBackgroundTasks(timeout: Duration = Duration.ofMinutes(5)) {
        step("Wait for background tasks to finish") {
            waitFor(duration = timeout, interval = Duration.ofSeconds(5)) {
                findAll<ComponentFixture>(byXpath("//div[@class='JProgressBar']")).isEmpty()
            }
        }
    }

    private fun isDumbMode(): Boolean = callJs("""
            var frameHelper = com.intellij.openapi.wm.impl.ProjectFrameHelper.getFrameHelper(component);
            com.intellij.openapi.project.DumbService.isDumb(frameHelper.project);
        """.trimIndent(), runInEdt = true)


    fun showCodeTesterExplorer() {
        if (findAll<CodeTesterExplorer>(CodeTesterExplorer::class.java).isEmpty()) {
            find(ComponentFixture::class.java,
                 byXpath("//div[@accessiblename='CodeTester' and @class='StripeButton' and @text='CodeTester']")).click()
        }
    }

    fun tryCloseTips() {
        step("Close Tip of the Day if it appears") {
            try {
                find<DialogFixture>(DialogFixture.byTitleContains("Tip")).close()
            } catch (e: Exception) {
            }
        }
    }


    fun Fixture.fetchScreenShot(): BufferedImage {
        return callJs<ByteArray>("""
            importPackage(java.io)
            importPackage(javax.imageio)
            const screenShot = new java.awt.Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            const location = component.getLocationOnScreen();
            const bounds = component.getBounds()
            const componentScreenShot = screenShot.getSubimage(location.x, location.y, bounds.getWidth(), bounds.getHeight());
            let pictureBytes;
            const baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(componentScreenShot, "png", baos);
                pictureBytes = baos.toByteArray();
            } finally {
              baos.close();
            }
            pictureBytes;
        """).inputStream().use {
            ImageIO.read(it)
        }
    }
}
