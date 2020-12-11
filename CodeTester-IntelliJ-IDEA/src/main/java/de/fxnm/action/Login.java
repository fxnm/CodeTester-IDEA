package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.service.AccountService;
import de.fxnm.service.ProjectStateService;
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
                    if (ProjectStateService.getService(project).isLoginConnectionEstablished()) {
                        AccountService.getService(project).asyncLogOut();
                    } else {
                        AccountService.getService(project).asyncLogIn();
                    }
                });
            } catch (final Throwable e) {
                LOG.error("Login / Logout action failed", e);
            }
        });
    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                super.update(event);

                final Presentation presentation = event.getPresentation();
                final ProjectStateService projectState = ProjectStateService.getService(project);

                presentation.setEnabled(projectState.isServerConnectionEstablished()
                        && projectState.isManualLoginLogoutConfig());

                if (ProjectStateService.getService(project).isLoginConnectionEstablished()) {
                    presentation.setIcon(PluginIcons.LOGIN_GREEN);
                    presentation.setDescription(CodeTesterBundle.message("plugin.action.login.logout.description"));
                    presentation.setText(CodeTesterBundle.message("plugin.action.login.logout.text"));

                } else {
                    presentation.setIcon(PluginIcons.LOGIN_RED);
                    presentation.setDescription(CodeTesterBundle.message("plugin.action.login.login.description"));
                    presentation.setText(CodeTesterBundle.message("plugin.action.login.login.text"));
                }

            } catch (final Throwable e) {
                LOG.error("Login / Logout action Update failed", e);
            }
        });
    }
}
