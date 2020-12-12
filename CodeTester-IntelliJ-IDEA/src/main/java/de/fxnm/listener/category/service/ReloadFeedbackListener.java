package de.fxnm.listener.category.service;

import com.intellij.openapi.project.Project;

import de.fxnm.listener.FeedbackListener;
import de.fxnm.service.ProjectStateService;
import de.fxnm.web.components.category.Category;

public class ReloadFeedbackListener extends FeedbackListener {

    public ReloadFeedbackListener(final Project project) {
        super(project);
    }

    @Override
    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
        });

        ProjectStateService.getService(this.project()).setManualLoginLogoutConfig(false);
        ProjectStateService.getService(this.project()).setManualRunConfig(false);
    }

    @Override
    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
            codeTesterToolWindowPanel.setCategories((Category[]) argumentOne);
        });

        ProjectStateService.getService(this.project()).setManualLoginLogoutConfig(true);
        ProjectStateService.getService(this.project()).setManualRunConfig(true);
    }


    @Override
    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayErrorMessage(true, toolWindowMessage);
        });

        ProjectStateService.getService(this.project()).setManualLoginLogoutConfig(true);
        ProjectStateService.getService(this.project()).setManualRunConfig(true);
    }
}