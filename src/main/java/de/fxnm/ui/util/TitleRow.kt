package de.fxnm.ui.util

import com.intellij.ui.layout.panel
import javax.swing.JComponent
import javax.swing.JPanel

class TitleRow {
    companion object {
        fun getTitleRow(title: String, componentToAdd: JComponent): JPanel {
            return panel {
                titledRow(title) {
                    row {
                        component(componentToAdd)
                    }
                }
            }
        }

        fun getHideableTitleRow(title: String, componentToAdd: JComponent): JPanel {

            // FIXME: 25.11.2020 Some strange bug with the hideableRow and its closing and opening behavior

            return panel {
                hideableRow(title) {
                    row {
                        component(componentToAdd)
                    }
                }.subRowsVisible = true

                row {
                    component(componentToAdd)
                }
            }
        }
    }
}
