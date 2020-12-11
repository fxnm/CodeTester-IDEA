package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import de.fxnm.service.ProjectStateService;
import de.fxnm.toolwindow.ToolWindowAccess;


public class ShowErrorTest extends BaseToggleAction {

    private static final Logger LOG = Logger.getInstance(ShowErrorTest.class);

    @Override
    public boolean isSelected(@NotNull final AnActionEvent event) {
        final Project project = getEventProject(event);
        if (project == null) {
            return false;
        }
        return ProjectStateService.getService(project).isDisplayingError();
    }

    @Override
    public void setSelected(@NotNull final AnActionEvent event, final boolean displayingErrors) {
        this.project(event).ifPresent(project -> {
            try {
                ProjectStateService.getService(project).setDisplayingError(displayingErrors);
                final boolean displayingSuccess = ProjectStateService.getService(project).isDisplayingSuccess();


                ToolWindowAccess.actOnToolWindowPanel(ToolWindowAccess.toolWindow(project),
                        codeTesterToolWindowPanel -> {
                            codeTesterToolWindowPanel.filterDisplayedResults(displayingErrors, displayingSuccess);
                        });

            } catch (final Throwable e) {
                LOG.error("Run Test Update failed");
            }
        });
    }
}
