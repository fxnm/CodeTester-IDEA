package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static java.util.Optional.ofNullable;

public abstract class BaseAction extends AnAction {

    private static final Logger LOG = Logger.getInstance(BaseAction.class);

    @Override
    public void update(final @NotNull AnActionEvent event) {
        try {
            final Project project = getEventProject(event);
            final Presentation presentation = event.getPresentation();

            if (project == null) {
                presentation.setEnabled(false);
                presentation.setVisible(false);
                LOG.warn("Action update failed but keep secret");
                return;
            }

            super.update(event);
            presentation.setVisible(true);

        } catch (final Throwable e) {
            LOG.warn("Action update failed", e);
        }
    }

    protected Optional<Project> project(@NotNull final AnActionEvent event) {
        return ofNullable(getEventProject(event));
    }
}
