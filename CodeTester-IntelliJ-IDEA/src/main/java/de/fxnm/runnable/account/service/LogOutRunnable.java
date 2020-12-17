package de.fxnm.runnable.account.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.toolwindow.CodeTesterToolWindowManager;
import de.fxnm.util.CodeTesterBundle;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_KEY;

public class LogOutRunnable extends BaseRunnable {

    public LogOutRunnable(final Project project) {
        super(project, LogOutRunnable.class);
    }

    @Override
    public void run() {
        try {
            super.startRunnable(CodeTesterBundle.message("plugin.runnable.logout.start.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.logout.start.toolWindowMessage"),
                    CodeTesterBundle.message("plugin.runnable.logout.start.backgroundProcessName"));
            this.logout();
            super.finishedRunnable(CodeTesterBundle.message("plugin.runnable.logout.finished.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.logout.finished.toolWindowMessage"));
        } catch (final Throwable e) {
            this.failedRunnable(CodeTesterBundle.message("plugin.runnable.logout.failed.loggerMessage"),
                    CodeTesterBundle.message("plugin.runnable.logout.failed.toolWindowMessage"), e);
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
