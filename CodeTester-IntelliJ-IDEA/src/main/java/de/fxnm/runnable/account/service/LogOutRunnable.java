package de.fxnm.runnable.account.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.toolwindow.CodeTesterToolWindowManager;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_DATE;

public class LogOutRunnable extends BaseRunnable<LogOutRunnable> {

    public LogOutRunnable(final Project project) {
        super(project, LogOutRunnable.class);
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
            //Remove stored Password
            PasswordManager.remove(LOGIN_DATE);

            //Close all open Check Result Tool Windows
            CodeTesterToolWindowManager.getService(this.project()).closeAllResultToolWindows();
        });
    }
}
