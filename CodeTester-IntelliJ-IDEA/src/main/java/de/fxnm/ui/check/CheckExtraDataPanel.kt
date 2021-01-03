package de.fxnm.ui.check

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBEmptyBorder
import com.intellij.util.ui.UIUtil
import de.fxnm.util.CodeTesterBundle
import de.fxnm.web.components.submission.success.CheckFileData
import javax.swing.JComponent

/**
 * Class which is responsible for creating the visual representation of the additional files needed for the check.
 */
class CheckExtraDataPanel(private val checkFileData: CheckFileData) {

    fun asJComponent(): JComponent {
        val scrollPane = JBScrollPane()
        scrollPane.setViewportView(
            panel {
                if (checkFileData.isEmpty()) {
                    row {
                        label(CodeTesterBundle.message("plugin.ui.checkExtraDataPanel.isEmpty"),
                            UIUtil.ComponentStyle.REGULAR, UIUtil.FontColor.BRIGHTER)
                    }
                }

                for (content in checkFileData.contentArray) {
                    row {
                        label(content)
                    }
                }
            }
        )
        scrollPane.border = JBEmptyBorder(0)
        return scrollPane
    }
}
