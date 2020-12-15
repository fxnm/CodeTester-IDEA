package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsData;
import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;
import de.fxnm.service.AccountService;
import de.fxnm.toolwindow.ToolWindowAccess;
import de.fxnm.util.CodeTesterBundle;
import icons.PluginIcons;

public class Login extends BaseAction {

    private static final Logger LOG = Logger.getInstance(Login.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        super.update(event);


        this.project(event).ifPresent(project -> {
            try {
                ToolWindowAccess.toolWindow(project).activate(() -> {
                    if (ProjectTransientSettingsService.getService(project).getState().getLoggedIn()) {
                        AccountService.getService(project).asyncLogOut();
                    } else {
                        AccountService.getService(project).asyncLogIn();
                    }
                });
            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.login/logout.actionFailed"), e);
            }
        });
    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                super.update(event);

                final Presentation presentation = event.getPresentation();
                @NotNull final ProjectTransientSettingsData settingsData = ProjectTransientSettingsService.getService(project).getState();

                presentation.setEnabled(settingsData.getInternetConnectionToCodeTester()
                        && settingsData.getLoginLogoutPossible());

                if (settingsData.getLoggedIn()) {
                    presentation.setIcon(PluginIcons.LOGIN_GREEN);
                    presentation.setDescription(CodeTesterBundle.message("plugin.action.login/logout.logout.description"));
                    presentation.setText(CodeTesterBundle.message("plugin.action.login/logout.logout.text"));

                } else {
                    presentation.setIcon(PluginIcons.LOGIN_RED);
                    presentation.setDescription(CodeTesterBundle.message("plugin.action.login/logout.login.description"));
                    presentation.setText(CodeTesterBundle.message("plugin.action.login/logout.login.text"));
                }

            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.login/logout.updateFailed"), e);
            }
        });
    }
}
