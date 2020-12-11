package de.fxnm.runnable.account.service;

import com.intellij.credentialStore.Credentials;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.exceptions.UsernamePasswordException;
import de.fxnm.runnable.BaseRunnable;
import de.fxnm.ui.account.LoginDialog;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.components.token.LoginToken;
import de.fxnm.web.grabber.access_token.LoginTokenGrabber;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_KEY;
import static de.fxnm.config.settings.password_safe.PasswordManager.TEST_LOGIN_KEY;

public class LogInRunnable extends BaseRunnable<LogInRunnable> {

    private boolean loginAbleWithSaveCredentials = false;

    public LogInRunnable(final Project project) {
        super(project, LogInRunnable.class);

        if (this.tryLoginWithPasswordSafeCredentials()) {
            this.loginAbleWithSaveCredentials = true;
        }

    }

    @Override
    public void run() {
        super.startRunnable("Logging in...", "Trying to login");

        if (this.loginAbleWithSaveCredentials) {
            super.finishedRunnable("Login successful");
            return;
        }

        ApplicationManager.getApplication().invokeAndWait(() -> {
            try {
                final Pair<String, String> cred = this.createLoginForm();

                PasswordManager.store(LOGIN_KEY, cred.first, cred.second);

                if (!this.tryLoginWithPasswordSafeCredentials()) {
                    throw new PasswordSafeException("Wrong password");
                }

                super.finishedRunnable("Login successful");

            } catch (final UsernamePasswordException e) {
                super.failedRunnable("Login abort");
            } catch (final Throwable e) {
                super.failedRunnable(e, "Failed to login");
                LOG.warn(e);
            }
        });
    }

    private boolean tryLoginWithPasswordSafeCredentials() {
        final LoginToken loginToken;
        try {
            final Credentials credentials = PasswordManager.retrieve(LOGIN_KEY);
            loginToken = LoginTokenGrabber.getToken(
                    this.project(),
                    Objects.requireNonNull(credentials.getUserName()),
                    Objects.requireNonNull(credentials.getPasswordAsString()));

        } catch (final PasswordSafeException | IOException | InternetConnectionException passwordSafeException) {
            return false;
        }

        return loginToken != null;
    }

    private Pair<String, String> createLoginForm() throws UsernamePasswordException, PasswordSafeException {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            @NotNull final Credentials credentials = PasswordManager.retrieve(TEST_LOGIN_KEY);
            return new Pair<>(credentials.getUserName(), credentials.getPasswordAsString());
        }

        final LoginDialog dialogWrapper = new LoginDialog(CodeTesterBundle.message("plugin.runner.LoginRunner.Title"));
        if (dialogWrapper.showAndGet()) {
            return dialogWrapper.getUserInput();
        } else {
            throw new UsernamePasswordException("No Username and Password provided");
        }
    }
}
