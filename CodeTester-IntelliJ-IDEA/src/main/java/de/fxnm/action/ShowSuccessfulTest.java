package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import de.fxnm.service.ProjectStateService;
import de.fxnm.toolwindow.ToolWindowAccess;

public class ShowSuccessfulTest extends BaseToggleAction {
    private static final Logger LOG = Logger.getInstance(ShowSuccessfulTest.class);

    @Override
    public boolean isSelected(@NotNull final AnActionEvent event) {
        final Project project = getEventProject(event);
        if (project == null) {
            return false;
        }
        return ProjectStateService.getService(project).isDisplayingSuccess();
    }

    @Override
    public void setSelected(@NotNull final AnActionEvent event, final boolean displayingSuccess) {
        this.project(event).ifPresent(project -> {
            try {
                ProjectStateService.getService(project).setDisplayingSuccess(displayingSuccess);
                final boolean displayingErrors = ProjectStateService.getService(project).isDisplayingError();


                ToolWindowAccess.actOnToolWindowPanel(ToolWindowAccess.toolWindow(project),
                        codeTesterToolWindowPanel -> {
                            codeTesterToolWindowPanel.filterDisplayedResults(displayingErrors, displayingSuccess);
                        });
            } catch (final Throwable e) {
                LOG.error("Show Successful Test Action Update failed", e);
            }
        });
    }
}
