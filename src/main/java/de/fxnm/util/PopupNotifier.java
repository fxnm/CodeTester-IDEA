package de.fxnm.util;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;

import javax.swing.Icon;

public final class PopupNotifier {
    private static final String NOTIFICATION_GROUP = "CodeTesterNotifications.Error";

    private PopupNotifier() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void notify(final Project project, final String title, final String subtitle, final String content,
                              final NotificationType notificationType, final Icon icon, final AnAction fixAction) {
        final Notification notification = new Notification(
                NOTIFICATION_GROUP,
                icon,
                title,
                subtitle,
                content,
                notificationType,
                null);
        notification.addAction(fixAction);
        notification.notify(project);
    }


    public static void notify(final Project project, final String title, final String subtitle, final String content,
                              final NotificationType notificationType, final Icon icon) {
        final Notification notification = new Notification(
                NOTIFICATION_GROUP,
                icon,
                title,
                subtitle,
                content,
                notificationType,
                null);
        notification.notify(project);
    }

    /*
    new AnAction("FIX ME") {
            @Override
            public void actionPerformed(@NotNull final AnActionEvent e) {
                System.out.println("hi");
                notification.expire();
            }
        });
     */
}
