package de.fxnm.ui.check

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBEmptyBorder
import com.intellij.util.ui.JBUI
import de.fxnm.web.components.submission.success.CheckOutputLineData
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Class which is responsible for creating the visual representation of the overview of the results of all the checks.
 */
class CheckInOutResultPanel(private val resultLines: Array<CheckOutputLineData>) {

    private val panel: JPanel

    val asScrollPanel: JComponent
        get() {
            val scrollPane = JBScrollPane()
            scrollPane.setViewportView(panel)
            scrollPane.border = JBEmptyBorder(0)
            return scrollPane
        }

    init {
        panel = panel {
            for (line in resultLines) {
                row(line.type) {
                    label(line.getContent())
                }
            }
        }

        panel.border = JBUI.Borders.empty(0)
    }
}
