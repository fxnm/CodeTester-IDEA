package de.fxnm.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import java.awt.Desktop;
import java.net.URI;

import de.fxnm.service.ProjectStateService;
import de.fxnm.web.grabber.CommonUrl;

public class NewTest extends BaseAction {

    public static final Logger LOG = Logger.getInstance(NewTest.class);

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        String url = "";
        try {
            final Desktop desktop = Desktop.getDesktop();
            url = CommonUrl.CREATE_NEW_TEST.getUrl(event.getProject());

            desktop.browse(new URI(url));
        } catch (final Throwable e) {
            LOG.error("New Test Action failed", e, url);
        }

    }

    @Override
    public void update(final @NotNull AnActionEvent event) {
        this.project(event).ifPresent(project -> {
            try {
                super.update(event);

                final Presentation presentation = event.getPresentation();
                final ProjectStateService projectStateService = ProjectStateService.getService(project);

                presentation.setEnabled(
                        projectStateService.isServerConnectionEstablished()
                                && Desktop.isDesktopSupported());

            } catch (final Throwable e) {
                LOG.error("New Test Action Update failed", e);
            }
        });
    }
}
