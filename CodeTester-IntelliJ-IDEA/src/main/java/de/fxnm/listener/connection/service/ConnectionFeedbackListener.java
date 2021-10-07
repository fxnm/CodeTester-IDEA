package de.fxnm.listener.connection.service;

import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

import de.fxnm.config.settings.project.transientstate.ProjectTransientSettingsService;
import de.fxnm.listener.FeedbackListener;
import de.fxnm.util.PopupNotifier;
import icons.PluginIcons;

public class ConnectionFeedbackListener extends FeedbackListener {

    public ConnectionFeedbackListener(final Project project) {
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

        ProjectTransientSettingsService.getService(this.project()).getState().setInternetConnectionToCodeTester(true);
    }

    @Override
    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
        this.toolWindow().ifPresent(codeTesterToolWindowPanel -> {
            codeTesterToolWindowPanel.displayErrorMessage(false, toolWindowMessage);
        });

        PopupNotifier.notify(this.project(), toolWindowMessage,
                (String) argumentOne,
                NotificationType.ERROR,
                PluginIcons.STATUS_ERROR);

        ProjectTransientSettingsService.getService(this.project()).getState().setInternetConnectionToCodeTester(false);
    }
}
