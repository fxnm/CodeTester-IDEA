package de.fxnm.config.password_safe;

import com.intellij.credentialStore.Credentials;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.Assertions;

import de.fxnm.config.settings.password_safe.Key;
import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.PasswordSafeException;

import static de.fxnm.CodeTesterPlugin.PLUGIN_ID;

public class PasswordManagerTest extends LightJavaCodeInsightFixtureTestCase {

    private final Key testKey = new Key(PLUGIN_ID + "PasswordManagerTest");

    public void testAddAndReceivePasswords() throws PasswordSafeException {
        final int[] length = {2, 5, 10, 20};
        for (final int l : length) {
            this.addAnReceivePasswords(l);
        }
    }

    private void addAnReceivePasswords(final int length) throws PasswordSafeException {
        final String username = RandomStringUtils.randomAlphanumeric(length);
        final String password = RandomStringUtils.randomAlphanumeric(length);

        PasswordManager.store(this.testKey, username, password);
        final Credentials credentials = PasswordManager.retrieve(this.testKey);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(username, credentials.getUserName());
            Assertions.assertEquals(password, credentials.getPasswordAsString());
        });
    }
}
