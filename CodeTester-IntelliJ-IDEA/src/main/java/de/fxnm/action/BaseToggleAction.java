package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public abstract class BaseToggleAction extends ToggleAction {

    protected Optional<Project> project(@NotNull final AnActionEvent event) {
        return ofNullable(getEventProject(event));
    }
}
