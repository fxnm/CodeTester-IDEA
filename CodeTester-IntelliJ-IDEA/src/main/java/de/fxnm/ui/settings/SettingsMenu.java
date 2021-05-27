package de.fxnm.ui.settings;

import com.intellij.credentialStore.Credentials;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import javax.swing.JComponent;

import de.fxnm.config.settings.password_safe.PasswordManager;
import de.fxnm.exceptions.PasswordSafeException;
import de.fxnm.util.CodeTesterBundle;

public class SettingsMenu implements SearchableConfigurable {

    private static final Logger LOG = Logger.getInstance(SettingsMenu.class);

    private final Project project;
    private final SettingsMenuPanel settingsMenuPanel;

    public SettingsMenu(final Project project) {
        this.project = project;
        this.settingsMenuPanel = new SettingsMenuPanel(this.project);
    }

    @Override
    public String getDisplayName() {
        return CodeTesterBundle.message("plugin.ui.settingsMenu.name");
    }


    @Override
    public @Nullable JComponent createComponent() {
        try {
            final Credentials credentials = PasswordManager.retrieve(PasswordManager.LOGIN_KEY);
            this.settingsMenuPanel.setCurrentLoggedInAccount(Objects.requireNonNull(credentials.getUserName()));
        } catch (final PasswordSafeException exception) {
            LOG.info("Settings Menu get current user failed", exception);
        }
        return this.settingsMenuPanel.creatUI();
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() {

    }

    @Override
    public @NotNull String getId() {
        return CodeTesterBundle.message("plugin.ui.settingsMenu.id");
    }
}
