package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsData;
import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;
import de.fxnm.service.ScannerService;
import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.toolwindow.main.toolwindow.CodeTesterToolWindowPanel;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.components.category.Category;

public class RunChecks extends BaseAction {
    private static final Logger LOG = Logger.getInstance(RunChecks.class);


    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        final Category category = ToolWindowAccess.getFromToolWindowPanel(
                ToolWindowAccess.toolWindow(event.getProject()),
                CodeTesterToolWindowPanel::getCurrentSelectedCategory, new Category(-1, null));

        if (category.getId() == -1) {
            LOG.error(CodeTesterBundle.message("plugin.action.runChecks.invalidCategory"),
                    category.errorDetails());
            return;
        }

        this.project(event).ifPresent(project -> {
            try {
                ToolWindowAccess.toolWindow(project).activate(() -> {

                    ScannerService.getService(project).asyncScanFiles(category.getId());
                });
            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.runChecks.actionFailed"),
                        e,
                        category.toString());
            }
        });
    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                super.update(event);

                final Presentation presentation = event.getPresentation();
                final @NotNull ProjectTransientSettingsData settingsData =
                        ProjectTransientSettingsService.getService(project).getState();

                presentation.setEnabled(settingsData.getInternetConnectionToCodeTester()
                        && settingsData.getLoggedIn()
                        && settingsData.getRunPossible()
                        && !ScannerService.getService(project).isCheckInProgress());

            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.runChecks.updateFailed"), e);
            }
        });
    }
}
