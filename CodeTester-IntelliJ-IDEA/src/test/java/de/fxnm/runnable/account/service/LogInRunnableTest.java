package de.fxnm.runnable.account.service;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import org.junit.jupiter.api.Assertions;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.listener.Listener;
import de.fxnm.util.EnvironmentVariable;

public class LogInRunnableTest extends LightJavaCodeInsightFixtureTestCase {

    private static final String NOTHING = " ";
    private LogInRunnable logInRunnable;


    public void testLoginWithNoCredentials() {
        this.logInRunnable = new LogInRunnable(this.getProject());
        this.logInRunnable.addListener(
                new Listener(this.getProject()) {
                    @Override
                    public void scanStartingImp(final Object... details) {
                    }

                    @Override
                    public void scanCompletedImp(final Object... details) {
                        Assertions.fail();
                    }

                    @Override
                    public void scanFailedImp(final Object... details) {
                    }
                });

        this.logInRunnable.run();
    }


    public void testLoginWithWrongUsername() {
        PasswordManager.store(PasswordManager.LOGIN_KEY, NOTHING, EnvironmentVariable.get(EnvironmentVariable.PASSWORD));

        this.logInRunnable = new LogInRunnable(this.getProject());
        this.logInRunnable.addListener(
                new Listener(this.getProject()) {
                    @Override
                    public void scanStartingImp(final Object... details) {
                    }

                    @Override
                    public void scanCompletedImp(final Object... details) {
                        Assertions.fail();
                    }

                    @Override
                    public void scanFailedImp(final Object... details) {
                        System.out.println(details[0]);
                    }
                }
        );
        this.logInRunnable.run();
    }


    public void testLoginWithWrongPassword() {
        PasswordManager.store(PasswordManager.LOGIN_KEY, EnvironmentVariable.get(EnvironmentVariable.USERNAME), NOTHING);

        this.logInRunnable = new LogInRunnable(this.getProject());
        this.logInRunnable.addListener(
                new Listener(this.getProject()) {
                    @Override
                    public void scanStartingImp(final Object... details) {
                    }

                    @Override
                    public void scanCompletedImp(final Object... details) {
                        Assertions.fail();
                    }

                    @Override
                    public void scanFailedImp(final Object... details) {

                    }
                }
        );
        this.logInRunnable.run();
    }


    public void testLoginCorrectCredentials() {
        PasswordManager.store(PasswordManager.LOGIN_KEY, EnvironmentVariable.get(EnvironmentVariable.USERNAME), EnvironmentVariable.get(EnvironmentVariable.PASSWORD));

        this.logInRunnable = new LogInRunnable(this.getProject());
        this.logInRunnable.addListener(
                new Listener(this.getProject()) {
                    @Override
                    public void scanStartingImp(final Object... details) {
                    }

                    @Override
                    public void scanCompletedImp(final Object... details) {
                    }

                    @Override
                    public void scanFailedImp(final Object... details) {
                        Assertions.fail();
                    }
                }
        );
        this.logInRunnable.run();
    }


    public void testLoginWithManualNoUsername() {
        PasswordManager.store(PasswordManager.TEST_LOGIN_KEY, NOTHING, EnvironmentVariable.get(EnvironmentVariable.PASSWORD));

        this.logInRunnable = new LogInRunnable(this.getProject());
        this.logInRunnable.addListener(
                new Listener(this.getProject()) {
                    @Override
                    public void scanStartingImp(final Object... details) {
                    }

                    @Override
                    public void scanCompletedImp(final Object... details) {
                        Assertions.fail();
                    }

                    @Override
                    public void scanFailedImp(final Object... details) {
                    }
                }
        );
        this.logInRunnable.run();
    }

    public void testLoginWithManualNoPassword() {
        PasswordManager.store(PasswordManager.TEST_LOGIN_KEY, EnvironmentVariable.get(EnvironmentVariable.USERNAME), NOTHING);

        this.logInRunnable = new LogInRunnable(this.getProject());
        this.logInRunnable.addListener(
                new Listener(this.getProject()) {
                    @Override
                    public void scanStartingImp(final Object... details) {
                    }

                    @Override
                    public void scanCompletedImp(final Object... details) {
                        Assertions.fail();
                    }

                    @Override
                    public void scanFailedImp(final Object... details) {

                    }
                }
        );
        this.logInRunnable.run();
    }


    public void testLoginWithManualCredentials() {
        PasswordManager.store(PasswordManager.TEST_LOGIN_KEY, EnvironmentVariable.get(EnvironmentVariable.USERNAME), EnvironmentVariable.get(EnvironmentVariable.PASSWORD));

        this.logInRunnable = new LogInRunnable(this.getProject());
        this.logInRunnable.addListener(
                new Listener(this.getProject()) {
                    @Override
                    public void scanStartingImp(final Object... details) {
                    }

                    @Override
                    public void scanCompletedImp(final Object... details) {
                    }

                    @Override
                    public void scanFailedImp(final Object... details) {
                        Assertions.fail();
                    }
                }
        );
        this.logInRunnable.run();
    }

}