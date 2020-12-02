package de.fxnm.runnable.account.service;

import com.intellij.credentialStore.Credentials;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;

import java.io.IOException;
import java.util.Objects;

import de.fxnm.runnable.BaseRunnable;
import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.InternetConnectionException;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.exceptions.UsernamePasswordException;
import de.fxnm.ui.account.LoginDialog;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.web.components.token.LoginToken;
import de.fxnm.web.grabber.access_token.LoginTokenGrabber;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_DATE;

public class LogInRunnable extends BaseRunnable {

    private static final Logger LOG = Logger.getInstance(LogInRunnable.class);
    private boolean loginAbleWithSaveCredentials = false;

    public LogInRunnable(final Project project) {
        super(project);

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

        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                final Pair<String, String> cred = this.createLoginForm();

                PasswordManager.store(LOGIN_DATE, cred.first, cred.second);

                if (!this.tryLoginWithPasswordSafeCredentials()) {
                    throw new PasswordSafeException("Wrong password");
                }

                super.finishedRunnable("Login successful");

            } catch (final UsernamePasswordException e) {
                super.failedRunnable("Login abort");
            } catch (final Throwable e) {
                super.failedRunnable("Failed to login");
                LOG.warn(e);
            }
        });
    }

    private boolean tryLoginWithPasswordSafeCredentials() {
        final LoginToken loginToken;
        try {
            final Credentials credentials = PasswordManager.retrieve(LOGIN_DATE);
            loginToken = LoginTokenGrabber.getToken(
                    this.project(),
                    Objects.requireNonNull(credentials.getUserName()),
                    Objects.requireNonNull(credentials.getPasswordAsString()));

        } catch (final PasswordSafeException | IOException | InternetConnectionException passwordSafeException) {
            return false;
        }

        return loginToken != null;
    }

    private Pair<String, String> createLoginForm() throws UsernamePasswordException {
        final LoginDialog dialogWrapper =
                new LoginDialog(CodeTesterBundle.message("plugin.runner.LoginRunner.Title"));
        if (dialogWrapper.showAndGet()) {
            return dialogWrapper.getUserInput();
        } else {
            throw new UsernamePasswordException("No Username and Password provided");
        }
    }
}