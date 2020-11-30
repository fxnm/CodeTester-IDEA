package de.fxnm.ui.check

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBEmptyBorder
import com.intellij.util.ui.JBUI
import de.fxnm.util.CopyUtil
import de.fxnm.util.PopupNotifier
import de.fxnm.web.components.submission.success.CheckOutputLineData
import icons.PluginIcons
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JComponent
import javax.swing.JLabel
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
                val copyLabel = JLabel(PluginIcons.COPY);
                copyLabel.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent) {
                       CopyUtil.copyToClipBoard(line.getRawContent())
                    }
                })

                row(line.type) {
                    label(line.getContent())
                    right {
                        component(copyLabel)
                    }
                }
            }
        }

        panel.border = JBUI.Borders.empty(0)
    }
}
