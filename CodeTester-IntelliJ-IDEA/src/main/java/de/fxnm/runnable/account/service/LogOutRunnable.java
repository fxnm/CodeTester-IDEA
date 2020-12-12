package de.fxnm.runnable.account.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.toolwindow.CodeTesterToolWindowManager;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_KEY;

public class LogOutRunnable extends BaseRunnable<LogOutRunnable> {

    public LogOutRunnable(final Project project) {
        super(project, LogOutRunnable.class);
    }

    @Override
    public void run() {
        try {
            super.startRunnable("Starting LogOutRunnable", "Trying to logout", "Logging out...");
            this.logout();
            super.finishedRunnable("Logout was successful and Runnable finished", "Logout Successful");
        } catch (final Throwable e) {
            this.failedRunnable("LogOut Runnable Failed", "Logout Failed, try it again later", e);
        }
    }

    private void logout() {
        ApplicationManager.getApplication().invokeAndWait(() -> {
            //Remove stored Password
            PasswordManager.remove(LOGIN_KEY);

            //Close all open Check Result Tool Windows
            CodeTesterToolWindowManager.getService(this.project()).closeAllResultToolWindows();
        });
    }
}
