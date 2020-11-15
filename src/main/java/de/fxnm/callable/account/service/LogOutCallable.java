package de.fxnm.callable.account.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;

import de.fxnm.callable.BaseCallable;
import de.fxnm.config.settings.password_safe.PasswordManager;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_DATE;

public class LogOutCallable extends BaseCallable<Boolean> {

    public LogOutCallable(final Project project) {
        super(project);
    }

    @Override
    public Boolean call() {
        try {
            super.startCallable("Logging out...", "Log out started");
            this.logout();
            super.finishedCallable("Logout successful");
        } catch (final Throwable e) {
            this.failedCallable("Failed to logout");
            return false;
        }
        return true;
    }

    private void logout() {
        ApplicationManager.getApplication().invokeLater(() -> {
            PasswordManager.remove(LOGIN_DATE);
        });
    }
}
