package de.fxnm.listener.account.service;

import com.intellij.openapi.project.Project;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.service.ProjectStateService;

public class LogOutFeedbackListener extends FeedbackListener {

    public LogOutFeedbackListener(final Project project) {
        super(project);
    }

    @Override
    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.removeCheckResult();
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
        });
    }

    @Override
    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
        });

        ProjectStateService.getService(this.project()).setLoginConnectionEstablished(false, this.project());
    }

    @Override
    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayErrorMessage(true, toolWindowMessage);
        });
    }
}
