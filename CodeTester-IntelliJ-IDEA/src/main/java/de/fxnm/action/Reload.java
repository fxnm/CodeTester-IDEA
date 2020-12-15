package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsData;
import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;
import de.fxnm.service.CategoryService;
import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.util.CodeTesterBundle;

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
                LOG.error(CodeTesterBundle.message("plugin.action.reload.actionFailed"), e);
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

                presentation.setEnabled(
                        settingsData.getInternetConnectionToCodeTester() && settingsData.getLoggedIn());
            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.reload.updateFailed"), e);
            }
        });
    }
}
