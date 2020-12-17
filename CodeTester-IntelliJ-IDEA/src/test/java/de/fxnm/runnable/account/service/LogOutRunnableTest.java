package de.fxnm.runnable.account.service;

import com.github.hypfvieh.util.StringUtil;
import com.intellij.credentialStore.Credentials;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.listener.Listener;
import de.fxnm.util.CodeTesterBundle;

import static testing.Util.checkEquality;
import static testing.Util.setEmptyCredentials;

public class LogOutRunnableTest extends LightJavaCodeInsightFixtureTestCase {

    private LogOutRunnable logOutRunnable;

    public void testLogout() {
        this.logOutRunnable = new LogOutRunnable(this.getProject());
        this.logOutRunnable.addListener(new Listener(this.getProject()) {
            @Override
            public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.logout.start.toolWindowMessage"), toolWindowMessage,
                        CodeTesterBundle.message("plugin.runnable.logout.start.backgroundProcessName"), backGroundProcessName,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.logout.finished.toolWindowMessage"), toolWindowMessage,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                Assertions.fail();
            }
        });

        this.logOutRunnable.run();
    }

    public void testLogoutPasswordRemoval() {
        final String randomString = StringUtil.randomString(10);

        PasswordManager.store(PasswordManager.LOGIN_KEY, randomString, randomString);

        this.logOutRunnable = new LogOutRunnable(this.getProject());
        this.logOutRunnable.addListener(new Listener(this.getProject()) {
            @Override
            public void scanStartingImp(final String toolWindowMessage, final String backGroundProcessName, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.logout.start.toolWindowMessage"), toolWindowMessage,
                        CodeTesterBundle.message("plugin.runnable.logout.start.backgroundProcessName"), backGroundProcessName,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanCompletedImp(final String toolWindowMessage, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                checkEquality(
                        CodeTesterBundle.message("plugin.runnable.logout.finished.toolWindowMessage"), toolWindowMessage,
                        null, argumentOne,
                        null, argumentTwo,
                        null, argumentThree
                );
            }

            @Override
            public void scanFailedImp(final String toolWindowMessage, final Throwable throwable, final Object argumentOne, final Object argumentTwo, final Object argumentThree) {
                Assertions.fail();
            }
        });

        this.logOutRunnable.run();

        Assertions.assertThrows(PasswordSafeException.class, () -> {
            @NotNull final Credentials c = PasswordManager.retrieve(PasswordManager.LOGIN_KEY);
        });
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setEmptyCredentials();
    }
}