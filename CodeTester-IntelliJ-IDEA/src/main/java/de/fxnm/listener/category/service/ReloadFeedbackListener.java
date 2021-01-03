package de.fxnm.listener.category.service;

import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;

import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsData;
import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;
import de.fxnm.listener.FeedbackListener;
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

        @NotNull final ProjectTransientSettingsData settingsData = ProjectTransientSettingsService.getService(this.project()).getState();
        settingsData.setLoginLogoutPossible(false);
        settingsData.setRunPossible(false);
    }

    @Override
    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayInfoMessage(true, toolWindowMessage);
            codeTesterToolWindowPanel.getCategories().setNewCategories((Category[]) argumentOne);
        });
        @NotNull final ProjectTransientSettingsData settingsData = ProjectTransientSettingsService.getService(this.project()).getState();
        settingsData.setLoginLogoutPossible(true);
        settingsData.setRunPossible(true);
    }


    @Override
    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayErrorMessage(true, toolWindowMessage);
            codeTesterToolWindowPanel.getCategories().setReloadFailed();
        });

        @NotNull final ProjectTransientSettingsData settingsData = ProjectTransientSettingsService.getService(this.project()).getState();
        settingsData.setLoginLogoutPossible(true);
        settingsData.setRunPossible(true);
    }
}