package de.fxnm.listener.account.service;

import com.intellij.openapi.project.Project;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.service.ProjectStateService;

public class LogOutFeedbackListener extends FeedbackListener {

    public LogOutFeedbackListener(final Project project) {
        super(project);
    }

    @Override
    public void scanStartingImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().removeCheckResult();
            this.toolWindow().displayInfoMessage(true, "Logging out");
        }
    }


    @Override
    public void scanCompletedImp(final Object... details) {

        if (this.toolWindow() != null) {
            this.toolWindow().displayInfoMessage(true, "Logged out");
        }

        ProjectStateService.getService(this.project())
                .setLoginConnectionEstablished(false, this.project());
    }

    @Override
    public void scanFailedImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().displayErrorMessage(true, "Logout failed!");
        }
    }
}
