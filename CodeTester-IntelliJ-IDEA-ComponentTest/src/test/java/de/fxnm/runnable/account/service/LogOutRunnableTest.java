package component.runnable.account.service;

import com.github.hypfvieh.util.StringUtil;

import org.junit.jupiter.api.Assertions;

import de.fxnm.TestingBase;
import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.listener.Listener;
import de.fxnm.runnable.account.service.LogOutRunnable;
import de.fxnm.util.CodeTesterBundle;

import static de.fxnm.Util.checkEquality;

public class LogOutRunnableTest extends TestingBase {

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
            PasswordManager.retrieve(PasswordManager.LOGIN_KEY);
        });
    }
}
