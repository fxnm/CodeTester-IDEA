package de.fxnm.listener.account.service;

import com.intellij.openapi.project.Project;

import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;
import de.fxnm.listener.FeedbackListener;
import de.fxnm.service.CategoryService;

public class LogInFeedbackListener extends FeedbackListener {

    public LogInFeedbackListener(final Project project) {
        super(project);
    }

    @Override
    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
        });
    }

    @Override
    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
        });

        ProjectTransientSettingsService.getService(this.project()).getState().setLoggedIn(true);
        CategoryService.getService(this.project()).asyncReloadCategories();
    }

    @Override
    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayErrorMessage(true, toolWindowMessage);
        });
    }
}
