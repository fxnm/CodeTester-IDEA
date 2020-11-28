package de.fxnm.ui.util

import com.intellij.util.ui.JBEmptyBorder
import javax.swing.Box
import javax.swing.JComponent

class HorizontalBox {
    private val box: Box = Box.createHorizontalBox()

    @JvmOverloads
    fun addComponent(component: JComponent?, spaceToTheNextComponent: Int = DEFAULT_SPACER) {
        box.add(component)
        box.add(Box.createHorizontalStrut(spaceToTheNextComponent))
    }

    fun get(): Box {
        return box
    }

    companion object {
        const val DEFAULT_SPACER = 4
    }

    init {
        box.add(Box.createHorizontalStrut(DEFAULT_SPACER))
        box.border = JBEmptyBorder(0)
    }
}
