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
    public void scanStartingImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().displayInfoMessage(true, details[0].toString());
        }

        ProjectStateService.getService(this.project()).setManualLoginLogoutConfig(false);
        ProjectStateService.getService(this.project()).setManualRunConfig(false);
    }

    @Override
    public void scanCompletedImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().setCategories((Category[]) details[1]);
            this.toolWindow().displayInfoMessage(true, details[0].toString());
        }

        ProjectStateService.getService(this.project()).setManualLoginLogoutConfig(true);
        ProjectStateService.getService(this.project()).setManualRunConfig(true);
    }

    @Override
    public void scanFailedImp(final Object... details) {
        if (this.toolWindow() != null) {
            this.toolWindow().displayInfoMessage(true, details[0].toString());
        }
        ProjectStateService.getService(this.project()).setManualLoginLogoutConfig(true);
        ProjectStateService.getService(this.project()).setManualRunConfig(true);
    }
}
