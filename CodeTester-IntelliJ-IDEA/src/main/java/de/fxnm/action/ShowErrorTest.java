package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import de.fxnm.service.ProjectStateService;
import de.fxnm.toolwindow.ToolWindowAccess;


public class ShowErrorTest extends ToggleAction {

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
        final Project project = getEventProject(event);
        if (project == null) {
            return;
        }

        ProjectStateService.getService(project).setDisplayingError(displayingErrors);
        final boolean displayingSuccess = ProjectStateService.getService(project).isDisplayingSuccess();


        ToolWindowAccess.actOnToolWindowPanel(ToolWindowAccess.toolWindow(project),
                codeTesterToolWindowPanel -> {
                    codeTesterToolWindowPanel.filterDisplayedResults(displayingErrors, displayingSuccess);
                });
    }
}
