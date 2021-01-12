package de.fxnm.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.project.Project
import java.util.Optional

abstract class BaseToggleAction : ToggleAction() {

    protected fun project(event: AnActionEvent): Optional<Project> {
        return Optional.ofNullable(getEventProject(event))
    }
}
