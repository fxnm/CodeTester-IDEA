package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsData;
import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsService;
import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.util.CodeTesterBundle;


public class ShowErrorChecks extends BaseToggleAction {

    private static final Logger LOG = Logger.getInstance(ShowErrorChecks.class);

    @Override
    public boolean isSelected(@NotNull final AnActionEvent event) {
        final AtomicBoolean returnValue = new AtomicBoolean(false);

        this.project(event).ifPresent(project -> {
            try {
                returnValue.set(ProjectPersistentSettingsService.getService(project).getState()
                        .getDisplayFailedChecks());
            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.showErrorChecks.actionFailed"),
                        e);
            }
        });

        return returnValue.get();
    }

    @Override
    public void setSelected(@NotNull final AnActionEvent event, final boolean displayingErrors) {
        this.project(event).ifPresent(project -> {
            try {
                @NotNull final ProjectPersistentSettingsData settingsData =
                        ProjectPersistentSettingsService.getService(project).getState();

                settingsData.setDisplayFailedChecks(displayingErrors);
                final boolean displayingSuccess = settingsData.getDisplaySuccessfulChecks();

                ToolWindowAccess.actOnToolWindowPanel(ToolWindowAccess.toolWindow(project),
                        codeTesterToolWindowPanel -> {
                            codeTesterToolWindowPanel.filterDisplayedResults(
                                            displayingErrors, displayingSuccess);
                        });

            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.showErrorChecks.updateFailed"), e);
            }
        });
    }
}
