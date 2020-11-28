package de.fxnm.ui.errormessage

import java.awt.BorderLayout
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.JPanel

class ErrorMessage(private val icon: Icon, private val message: String, val autoRemove: Boolean) : JPanel(BorderLayout()) {
    init {
        this.add(JLabel(icon), BorderLayout.WEST)
        this.add(JLabel(message), BorderLayout.CENTER)
    }
}
