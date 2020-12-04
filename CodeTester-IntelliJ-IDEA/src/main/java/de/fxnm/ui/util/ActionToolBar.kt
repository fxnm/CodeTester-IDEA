package de.fxnm.ui.util

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import javax.swing.Box
import javax.swing.JPanel

class ActionToolBar(toolWindowName: String, actionToolbarName: String, horizontal: Boolean) : JPanel() {
    init {
        val actionGroup = ActionManager.getInstance().getAction(actionToolbarName) as ActionGroup
        val actionToolbar = ActionManager.getInstance()
                .createActionToolbar(toolWindowName, actionGroup, horizontal)
        val toolBox = Box.createHorizontalBox()
        toolBox.add(actionToolbar.component)
        this.add(toolBox)
    }
}
