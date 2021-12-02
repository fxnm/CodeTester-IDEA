package de.fxnm.util;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

import javax.swing.Icon;

public final class PopupNotifier {

    private static final String NOTIFICATION_GROUP = "CodeTesterNotifications";

    private PopupNotifier() throws IllegalAccessException {
        throw new IllegalAccessException();
    }


    public static void notify(final Project project, final String title, final String content,
                              final NotificationType notificationType, final Icon icon) {

        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP)
                .createNotification(title, content, notificationType)
                .setIcon(icon)
                .notify(project);
    }
}
