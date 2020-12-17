package de.fxnm.config.settings.password_safe;


import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.CredentialAttributesKt;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.util.CodeTesterBundle;

import static de.fxnm.CodeTesterPlugin.PLUGIN_ID;

public final class PasswordManager {

    public static final Key LOGIN_KEY = new Key(PLUGIN_ID + "-Login");
    public static final Key TEST_LOGIN_KEY = new Key(PLUGIN_ID + "-Test-Login");

    private static final Logger LOG = Logger.getInstance(PasswordManager.class);

    private PasswordManager() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    public static void store(final Key key, final String username, final String password) {
        final CredentialAttributes credentialAttributes = createCredentialAttributes(key);
        final Credentials credentials = new Credentials(username, password);

        PasswordSafe.getInstance().set(credentialAttributes, credentials);
        LOG.info(CodeTesterBundle.message("plugin.settings.passwordManager.store.successful") + key);
    }

    public static @NotNull Credentials retrieve(final Key key) throws PasswordSafeException {
        final CredentialAttributes credentialAttributes = createCredentialAttributes(key);

        final Credentials credentials = PasswordSafe.getInstance().get(credentialAttributes);

        if (credentials == null) {
            LOG.info(CodeTesterBundle.message("plugin.settings.passwordManager.retrieve.nullReturn") + key);
            throw new PasswordSafeException(CodeTesterBundle.message("plugin.settings.passwordManager.retrieve.nullReturnException")+ key);
        }

        LOG.info(CodeTesterBundle.message("plugin.settings.passwordManager.retrieve.successful") + key);

        return credentials;
    }

    public static void remove(final Key key) {
        final CredentialAttributes credentialAttributes = createCredentialAttributes(key);
        PasswordSafe.getInstance().set(credentialAttributes, null);

        LOG.info(CodeTesterBundle.message("plugin.settings.passwordManager.remove.successful") + key);
    }

    private static CredentialAttributes createCredentialAttributes(final Key key) {
        return new CredentialAttributes(CredentialAttributesKt.generateServiceName(
                PLUGIN_ID,
                key.toString()));
    }
}
