package de.fxnm.ui.check

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel
import com.intellij.util.ui.JBUI
import de.fxnm.util.CopyUtil
import de.fxnm.web.components.submission.success.CheckOutputLineData
import icons.PluginIcons
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.LinkedList
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane

/**
 * Class which is responsible for creating the visual representation of the overview of the results of all the checks.
 */
class CheckInOutResultPanel {

    private val scrollPane: JScrollPane = JBScrollPane()
    private var panel: JPanel = JPanel()
    private var resultLines: MutableList<CheckOutputLineData> = LinkedList()

    fun getComponent(): JComponent {
        return scrollPane
    }

    fun removeLines() {
        resultLines.clear()
        generateComponent()
    }

    fun addLines(resultLines: Array<CheckOutputLineData>) {
        this.resultLines = resultLines.toMutableList()
        generateComponent()
    }

    private fun generateComponent() {
        panel = panel {
            for (line in resultLines) {
                val copyLabel = JLabel(PluginIcons.COPY)
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

        panel.border = JBUI.Borders.empty()
        scrollPane.setViewportView(panel)
    }

    init {
        scrollPane.border = JBUI.Borders.empty()
    }
}
