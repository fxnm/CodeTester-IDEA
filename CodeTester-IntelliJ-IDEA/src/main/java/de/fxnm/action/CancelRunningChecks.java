package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.wm.ToolWindow;

import org.jetbrains.annotations.NotNull;

import de.fxnm.service.ScannerService;
import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.util.CodeTesterBundle;

public class CancelRunningChecks extends BaseAction {

    private static final Logger LOG = Logger.getInstance(CancelRunningChecks.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                final ToolWindow toolWindow = ToolWindowAccess.toolWindow(project);
                toolWindow.activate(() -> {

                    ScannerService.getService(project).stopChecks();

                });
            } catch (final Throwable e) {
                LOG.warn(CodeTesterBundle.message("plugin.action.cancelRunningChecks.actionFailed"), e);
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
                LOG.warn(CodeTesterBundle.message("plugin.action.cancelRunningChecks.updateFailed"), e);
            }
        });
    }
}
