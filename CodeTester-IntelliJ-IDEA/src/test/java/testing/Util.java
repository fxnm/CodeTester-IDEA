package testing;

import org.jetbrains.annotations.TestOnly;
import org.junit.jupiter.api.Assertions;

import de.fxnm.config.settings.password_safe.PasswordManager;

public final class Util {

    private static final String NOTHING = " ";

    private Util() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @TestOnly
    public static void checkEquality(final Object... objects) {
        if (objects.length % 2 != 0) {
            Assertions.fail("Invalid amount of Assertion Arguments");
        }

        for (int i = 0; i < objects.length; i += 2) {
            Assertions.assertEquals(objects[i], objects[i + 1]);
        }
    }


    @TestOnly
    public static void setEmptyCredentials() {
        PasswordManager.store(PasswordManager.LOGIN_KEY, NOTHING, NOTHING);
        PasswordManager.store(PasswordManager.TEST_LOGIN_KEY, NOTHING, NOTHING);
    }
}
