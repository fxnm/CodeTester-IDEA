package fxnm.config.password_safe;

import com.intellij.credentialStore.Credentials;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;

import de.fxnm.config.settings.password_safe.Key;
import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.PasswordSafeException;
import testing.TestingBase;

import static de.fxnm.config.settings.password_safe.PasswordManager.LOGIN_KEY;
import static de.fxnm.config.settings.password_safe.PasswordManager.TEST_LOGIN_KEY;

public class PasswordManagerTest extends TestingBase {

    private static final String NOTHING = " ";
    private static String RANDOM_PASSWORD = NOTHING;
    private static String RANDOM_USERNAME = NOTHING;

    public void testAddAnReceivePasswordsDefaultKey() throws PasswordSafeException {
        PasswordManager.store(LOGIN_KEY, RANDOM_USERNAME, RANDOM_PASSWORD);

        this.assertCredentials(LOGIN_KEY, RANDOM_USERNAME, RANDOM_PASSWORD);

    }

    public void testAddAnReceivePasswordsTestKey() throws PasswordSafeException {

        PasswordManager.store(TEST_LOGIN_KEY, RANDOM_USERNAME, RANDOM_PASSWORD);

        this.assertCredentials(TEST_LOGIN_KEY, RANDOM_USERNAME, RANDOM_PASSWORD);

    }

    public void testAddAndRemoveDifferentKey() throws PasswordSafeException {
        PasswordManager.store(LOGIN_KEY, RANDOM_USERNAME, RANDOM_PASSWORD);
        PasswordManager.store(TEST_LOGIN_KEY, NOTHING, NOTHING);
        PasswordManager.remove(TEST_LOGIN_KEY);

        this.assertCredentials(LOGIN_KEY, RANDOM_USERNAME, RANDOM_PASSWORD);
    }

    public void testOverwritePassword() throws PasswordSafeException {
        PasswordManager.store(LOGIN_KEY, NOTHING, NOTHING);
        this.assertCredentials(LOGIN_KEY, NOTHING, NOTHING);

        PasswordManager.store(LOGIN_KEY, RANDOM_USERNAME, RANDOM_PASSWORD);
        this.assertCredentials(LOGIN_KEY, RANDOM_USERNAME, RANDOM_PASSWORD);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        RANDOM_USERNAME = RandomStringUtils.randomAlphanumeric(10);
        RANDOM_PASSWORD = RandomStringUtils.randomAlphanumeric(10);

    }

    private void assertCredentials(final Key key, final String username, final String password) throws PasswordSafeException {
        final Credentials credentials = PasswordManager.retrieve(key);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(username, credentials.getUserName());
            Assertions.assertEquals(password, credentials.getPasswordAsString());
        });
    }
}
