package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.ToolWindow;

import org.jetbrains.annotations.NotNull;

import de.fxnm.exceptions.RunnableException;
import de.fxnm.service.ScannerService;
import de.fxnm.toolwindow.ToolWindowAccess;

public class StopTest extends BaseAction {

    private static final Logger LOG = Logger.getInstance(StopTest.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                final ToolWindow toolWindow = ToolWindowAccess.toolWindow(project);
                toolWindow.activate(() -> {
                    try {
                        ScannerService.getService(project).stopChecks();
                    } catch (final RunnableException e) {
                        LOG.error("No scan to be stopped", e);
                    }
                });
            } catch (final Throwable e) {
                LOG.warn("Abort Scan Action failed", e);
            }
        });
    }

    @Override
    public void update(final @NotNull AnActionEvent event) {


        this.project(event).ifPresent(project -> {
            try {
                super.update(event);
                final Presentation presentation = event.getPresentation();
                presentation.setEnabled(ScannerService.getService(project).isCheckInProgress());

            } catch (final Throwable e) {
                LOG.warn("Abort button update Action failed", e);
            }
        });
    }
}
