package de.fxnm.listener.scanner.service;

import com.intellij.openapi.project.Project;

import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;
import de.fxnm.listener.FeedbackListener;
import de.fxnm.toolwindow.CodeTesterToolWindowManager;
import de.fxnm.web.components.submission.SubmissionResult;

public class UiScannerFeedbackListener extends FeedbackListener {

    public UiScannerFeedbackListener(final Project project) {
        super(project);
    }

    @Override
    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.removeCheckResult();
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
        });

        CodeTesterToolWindowManager.getService(this.project()).newCheckRunning();
        ProjectTransientSettingsService.getService(this.project()).getState().setLoginLogoutPossible(false);
    }

    @Override
    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
            codeTesterToolWindowPanel.displayCheckResult((SubmissionResult) argumentOne, this.project());
        });

        CodeTesterToolWindowManager.getService(this.project()).newCheckCompleted((SubmissionResult) argumentOne);
        ProjectTransientSettingsService.getService(this.project()).getState().setLoginLogoutPossible(true);

    }

    @Override
    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayErrorMessage(true, toolWindowMessage);
        });

        CodeTesterToolWindowManager.getService(this.project()).newCheckCompleted((SubmissionResult) argumentOne);
        ProjectTransientSettingsService.getService(this.project()).getState().setLoginLogoutPossible(true);
    }
}
