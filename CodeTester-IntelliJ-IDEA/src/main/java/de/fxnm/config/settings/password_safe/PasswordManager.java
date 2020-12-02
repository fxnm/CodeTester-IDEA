package de.fxnm.config.settings.password_safe;


import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;

import org.jetbrains.annotations.NotNull;

import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.util.CodeTesterBundle;

import static de.fxnm.CodeTesterPlugin.PLUGIN_ID;

public final class PasswordManager {

    public static final Key LOGIN_DATE = new Key(PLUGIN_ID + "Login");

    private PasswordManager() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void store(final Key key, final String username, final String password) {
        final CredentialAttributes credentialAttributes = createCredentialAttributes(key);
        final Credentials credentials = new Credentials(username, password);

        PasswordSafe.getInstance().set(credentialAttributes, credentials);
    }

    public static @NotNull Credentials retrieve(final Key key) throws PasswordSafeException {
        final CredentialAttributes credentialAttributes = createCredentialAttributes(key);

        final Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);

        if (credentials == null) {
            throw new PasswordSafeException(CodeTesterBundle.message("plugin.password.noSuchPassword"));
        }

        return credentials;
    }

    public static void remove(final Key key) {
        final CredentialAttributes credentialAttributes = createCredentialAttributes(key);
        PasswordSafe.getInstance().set(credentialAttributes, null);
    }

    private static CredentialAttributes createCredentialAttributes(final Key key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName(
                PLUGIN_ID,
                key.toString()));
    }
}
