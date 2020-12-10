package fxnm.config.password_safe;

import com.intellij.credentialStore.Credentials;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.PasswordSafeException;

import static de.fxnm.config.settings.password_safe.PasswordManager.TEST_LOGIN_KEY;

public class PasswordManagerTest extends LightJavaCodeInsightFixtureTestCase {

    public void testAddAndReceivePasswords() throws PasswordSafeException {
        final int[] length = {2, 5, 10, 20};
        for (final int l : length) {
            this.addAnReceivePasswords(l);
        }
    }

    private void addAnReceivePasswords(final int length) throws PasswordSafeException {
        final String username = RandomStringUtils.randomAlphanumeric(length);
        final String password = RandomStringUtils.randomAlphanumeric(length);

        PasswordManager.store(TEST_LOGIN_KEY, username, password);
        final Credentials credentials = PasswordManager.retrieve(TEST_LOGIN_KEY);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(username, credentials.getUserName());
            Assertions.assertEquals(password, credentials.getPasswordAsString());
        });
    }
}
