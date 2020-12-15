package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsData;
import de.fxnm.config.settings.project.persistentstate.ProjectPersistentSettingsService;
import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.util.CodeTesterBundle;

public class ShowSuccessfulChecks extends BaseToggleAction {
    private static final Logger LOG = Logger.getInstance(ShowSuccessfulChecks.class);

    @Override
    public boolean isSelected(@NotNull final AnActionEvent event) {
        final AtomicBoolean returnValue = new AtomicBoolean(false);

        this.project(event).ifPresent(project -> {
            try {
                returnValue.set(ProjectPersistentSettingsService.getService(project).getState().getDisplaySuccessfulChecks());
            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.showSuccessfulChecks.actionFailed"),
                        e);
            }
        });

        return returnValue.get();
    }

    @Override
    public void setSelected(@NotNull final AnActionEvent event, final boolean displayingSuccess) {
        this.project(event).ifPresent(project -> {
            try {
                @NotNull final ProjectPersistentSettingsData settingsData =
                        ProjectPersistentSettingsService.getService(project).getState();
                settingsData.setDisplaySuccessfulChecks(displayingSuccess);
                final boolean displayingErrors = settingsData.getDisplayFailedChecks();


                ToolWindowAccess.actOnToolWindowPanel(ToolWindowAccess.toolWindow(project),
                        codeTesterToolWindowPanel -> {
                            codeTesterToolWindowPanel.filterDisplayedResults(
                                    displayingErrors, displayingSuccess);
                        });
            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.showSuccessfulChecks.updateFailed"),
                        e);
            }
        });
    }
}
