package de.fxnm.runnable.account.service;

import de.fxnm.TestingBase;
import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.listener.Listener;
import de.fxnm.runnable.account.service.LogInRunnable;
import de.fxnm.util.CodeTesterBundle;
import de.fxnm.util.EnvironmentVariable;

import static de.fxnm.Util.checkEquality;

public class LogInRunnableTest extends TestingBase {

    private static final String NOTHING = " ";
    private LogInRunnable logInRunnable;

    public void testLoginWithNoCredentials() {
        this.logInRunnable = new LogInRunnable(this.getProject());
        this.logInRunnable.addListener(
                new Listener(this.getProject()) {
                    @Override
                    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.start.toolWindowMessage"), toolWindowMessage,
                                CodeTesterBundle.message("plugin.runnable.login.start.backgroundProcessName"), backGroundProcessName,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        fail();
                    }

                    @Override
                    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                "Invalid credentials", toolWindowMessage,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
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
                    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.start.toolWindowMessage"), toolWindowMessage,
                                CodeTesterBundle.message("plugin.runnable.login.start.backgroundProcessName"), backGroundProcessName,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        fail();
                    }

                    @Override
                    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                "Invalid credentials", toolWindowMessage,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
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
                    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.start.toolWindowMessage"), toolWindowMessage,
                                CodeTesterBundle.message("plugin.runnable.login.start.backgroundProcessName"), backGroundProcessName,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        fail();
                    }

                    @Override
                    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                "Invalid credentials", toolWindowMessage,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
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
                    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.start.toolWindowMessage"), toolWindowMessage,
                                CodeTesterBundle.message("plugin.runnable.login.start.backgroundProcessName"), backGroundProcessName,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.finished.previous.toolWindowMessage"),
                                toolWindowMessage,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        fail();
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
                    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.start.toolWindowMessage"), toolWindowMessage,
                                CodeTesterBundle.message("plugin.runnable.login.start.backgroundProcessName"), backGroundProcessName,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        fail();
                    }

                    @Override
                    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                "Invalid credentials", toolWindowMessage,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
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
                    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.start.toolWindowMessage"), toolWindowMessage,
                                CodeTesterBundle.message("plugin.runnable.login.start.backgroundProcessName"), backGroundProcessName,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        fail();
                    }

                    @Override
                    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                "Invalid credentials", toolWindowMessage,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
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
                    public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.start.toolWindowMessage"), toolWindowMessage,
                                CodeTesterBundle.message("plugin.runnable.login.start.backgroundProcessName"), backGroundProcessName,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        checkEquality(
                                CodeTesterBundle.message("plugin.runnable.login.finished.withUserInput.toolWindowMessage"), toolWindowMessage,
                                null, argumentOne,
                                null, argumentTwo,
                                null, argumentThree
                        );
                    }

                    @Override
                    public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                        fail();
                    }
                }
        );
        this.logInRunnable.run();
    }
}