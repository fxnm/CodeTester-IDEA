package de.fxnm.listener.account.service;

import com.intellij.openapi.project.Project;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.service.ProjectStateService;

public class LogInFeedbackListener extends FeedbackListener {

    public LogInFeedbackListener(final Project project) {
        super(project);
    }

    @Override
    public void scanStartingImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().displayInfoMessage(true, "Trying to login");
        }
    }

    @Override
    public void scanCompletedImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().displayInfoMessage(true, "Login successful");
        }

        ProjectStateService.getService(this.project())
                .setLoginConnectionEstablished(true, this.project());
    }

    @Override
    public void scanFailedImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().displayErrorMessage(true, "Login failed");
        }
    }
}
