package de.fxnm.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import de.fxnm.util.CodeTesterBundle
import java.util.*

abstract class BaseAction : AnAction() {

    override fun update(event: AnActionEvent) {
        try {
            val project = getEventProject(event)
            val presentation = event.presentation

            if (project == null) {
                presentation.isEnabled = false
                presentation.isVisible = false
                LOG.warn(CodeTesterBundle.message("plugin.action.base.updateFailed.silent"))
                return
            }

            super.update(event)
            presentation.isVisible = true
        } catch (e: Throwable) {
            LOG.warn(CodeTesterBundle.message("plugin.action.base.updateFailed"), e)
        }
    }

    protected fun project(event: AnActionEvent): Optional<Project> {
        return Optional.ofNullable(getEventProject(event))
    }

    companion object {
        private val LOG = Logger.getInstance(BaseAction::class.java)
    }
}
