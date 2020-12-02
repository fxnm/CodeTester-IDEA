package de.fxnm.runnable.account.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import de.fxnm.runnable.BaseRunnable;
import de.fxnm.config.settings.password_safe.PasswordManager;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_DATE;

public class LogOutRunnable extends BaseRunnable {

    public LogOutRunnable(final Project project) {
        super(project);
    }

    @Override
    public void run() {
        try {
            super.startRunnable("Logging out...", "Log out started");
            this.logout();
            super.finishedRunnable("Logout successful");
        } catch (final Throwable e) {
            this.failedRunnable("Failed to logout");
        }
    }

    private void logout() {
        ApplicationManager.getApplication().invokeLater(() -> {
            PasswordManager.remove(LOGIN_DATE);
        });
    }
}
