package de.fxnm.steps

import com.intellij.remoterobot.stepsProcessing.step
import de.fxnm.extensions.uiTest
import de.fxnm.fixtures.actionMenu
import de.fxnm.fixtures.actionMenuItem
import de.fxnm.fixtures.idea
import java.io.File
import javax.imageio.ImageIO

class IdeSteps {
    companion object {
        fun closeCurrentOpenProject() {
            uiTest {
                idea {
                    step("Close the current open project") {
                        actionMenu("File").click()
                        actionMenuItem("Close Project").click()
                    }
                }
            }
        }

        fun takeScreenShot(name: String, path:String) {
            File(path).mkdirs()

            uiTest {
                idea {
                    val bufferedImage = fetchScreenShot()
                    val outputfile = File("$path/${name}.jpg")
                    ImageIO.write(bufferedImage, "jpg", outputfile)
                }

            }
        }
    }
}
