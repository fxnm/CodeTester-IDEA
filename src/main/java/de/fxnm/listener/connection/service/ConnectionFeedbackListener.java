package de.fxnm.listener.connection.service;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.service.ProjectStateService;
import de.fxnm.util.PopupNotifier;
import icons.PluginIcons;

public class ConnectionFeedbackListener extends FeedbackListener {

    public ConnectionFeedbackListener(final Project project) {
        super(project);
    }

    @Override
    public void scanStartingImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().removeCheckResult();
            this.toolWindow().displayInfoMessage(true, details[0].toString());
        }
    }

    @Override
    public void scanCompletedImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().displayInfoMessage(true, details[0].toString());
        }

        ProjectStateService.getService(this.project())
                .setServerConnectionEstablished(true, this.project());
    }

    @Override
    public void scanFailedImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().displayErrorMessage(false, details[0].toString());
        }

        PopupNotifier.notify(this.project(), "Connection Failed", "", "", NotificationType.ERROR, PluginIcons.STATUS_ERROR);

        ProjectStateService.getService(this.project())
                .setServerConnectionEstablished(false, this.project());
    }
}
