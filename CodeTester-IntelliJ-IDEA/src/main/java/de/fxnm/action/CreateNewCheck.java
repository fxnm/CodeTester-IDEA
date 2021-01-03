package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.net.URI;

import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsData;
import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.grabber.CommonUrl;

public class CreateNewCheck extends BaseAction {

    public static final Logger LOG = Logger.getInstance(CreateNewCheck.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        String url = "";
        try {
            final Desktop desktop = Desktop.getDesktop();
            url = CommonUrl.CREATE_NEW_TEST.getUrl(event.getProject());

            desktop.browse(new URI(url));
        } catch (final Throwable e) {
            LOG.error(CodeTesterBundle.message("plugin.action.createNewChecks.actionFailed"), e, url);
        }

    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                super.update(event);

                final Presentation presentation = event.getPresentation();
                final ProjectTransientSettingsData settingsData =
                        ProjectTransientSettingsService.getService(project).getState();

                presentation.setEnabled(
                        settingsData.getInternetConnectionToCodeTester()
                                && Desktop.isDesktopSupported());

            } catch (final Throwable e) {
                LOG.error(CodeTesterBundle.message("plugin.action.createNewChecks.updateFailed"), e);
            }
        });
    }
}
