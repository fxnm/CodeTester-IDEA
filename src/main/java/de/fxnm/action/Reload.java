package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.service.CategoryService;
import de.fxnm.service.ProjectStateService;
import de.fxnm.toolwindow.ToolWindowAccess;

public class Reload extends BaseAction {

    private static final Logger LOG = Logger.getInstance(Reload.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                ToolWindowAccess.toolWindow(project).activate(() -> {
                    CategoryService.getService(project).asyncReloadCategories();
                });
            } catch (final Throwable e) {
                LOG.error("Reload Action failed", e);
            }
        });
    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
        super.update(event);

        final Presentation presentation = event.getPresentation();
        final ProjectStateService projectStateService = ProjectStateService.getService(event.getProject());

        presentation.setEnabled(
                projectStateService.isServerConnectionEstablished()
                        && projectStateService.isLoginConnectionEstablished());
    }
}
