package de.fxnm.ui.errormessage

import de.fxnm.ui.util.HorizontalComponentBox
import java.awt.BorderLayout
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.JPanel

class ErrorMessage(icon: Icon, message: String, val autoRemove: Boolean) : JPanel(BorderLayout()) {
    init {
        val horizontalComponentBox = HorizontalComponentBox()
        horizontalComponentBox.addComponent(JLabel(icon))
        horizontalComponentBox.addComponent(JLabel(message))
        this.add(horizontalComponentBox.get())
    }
}
