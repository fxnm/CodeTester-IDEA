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

public class LogInRunnable extends BaseRunnable {

    private boolean loginAbleWithSaveCredentials = false;

    public LogInRunnable(final Project project) {
        super(project, LogInRunnable.class);

        if (this.tryLoginWithPasswordSafeCredentials()) {
            this.loginAbleWithSaveCredentials = true;
        }
    }

    @Override
    public void run() {
        super.startRunnable("Starting LogInRunnable", "Trying to login", "Logging in...");

        if (this.loginAbleWithSaveCredentials) {
            super.finishedRunnable("Login successful with previously stored credentials", "Login Successful");
            return;
        }

        ApplicationManager.getApplication().invokeAndWait(() -> {
            try {
                final Pair<String, String> cred = this.createLoginForm();

                PasswordManager.store(LOGIN_KEY, cred.first, cred.second);

                if (!this.tryLoginWithPasswordSafeCredentials()) {
                    this.failedRunnable("LogIn Runnable Failed due to invalid user input credentials",
                            "Invalid credentials");
                    return;
                }

                super.finishedRunnable("LogIn Runnable was successful and Runnable finished", "Login successful");

            } catch (final UsernamePasswordException e) {
                super.failedRunnable("LogIn Runnable Failed due to no user provided credentials", "Login abort, try "
                        + "it again later");
            } catch (final Throwable e) {
                super.failedRunnable("LogIn Runnable Failed", "Login Failed, try it again later", e);
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
