package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.service.ProjectStateService;
import de.fxnm.service.ScannerService;
import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;
import de.fxnm.web.components.category.Category;

public class RunTest extends BaseAction {
    private static final Logger LOG = Logger.getInstance(RunTest.class);


    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        final Category category = ToolWindowAccess.getFromToolWindowPanel(ToolWindowAccess.toolWindow(event.getProject()),
                CodeTesterToolWindowPanel::getCurrentSelectedCategory, new Category(-1, null));

        if (category.getId() == -1) {
            LOG.error("Invalid  provided category",
                    "",
                    category.errorDetails());
            return;
        }

        this.project(event).ifPresent(project -> {
            try {
                ToolWindowAccess.toolWindow(project).activate(() -> {

                    ScannerService.getService(project).asyncScanFiles(category.getId());
                });
            } catch (final Throwable e) {
                LOG.error("Run Test Action failed", e, category.toString());
            }
        });
    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
        final Presentation presentation = event.getPresentation();
        final ProjectStateService projectStateService = ProjectStateService.getService(event.getProject());

        presentation.setEnabled(projectStateService.isServerConnectionEstablished()
                && projectStateService.isLoginConnectionEstablished()
                && !ScannerService.getService(event.getProject()).isCheckInProgress()
                && projectStateService.isManualRunConfig()
        );
    }
}
