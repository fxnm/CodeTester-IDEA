package de.fxnm.ui.util

import com.intellij.ui.layout.panel
import javax.swing.JComponent
import javax.swing.JPanel

class TitleRow {
    companion object {

        /**
         * Methode which creates a title row, with the component under it.
         */
        fun getTitleRow(title: String, componentToAdd: JComponent): JPanel {
            return panel {
                titledRow(title) {
                    row {
                        component(componentToAdd)
                    }
                }
            }
        }
    }
}
