package de.fxnm.config.settings.password_safe;

public class Key {
    private final String keyValue;

    public Key(final String keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public String toString() {
        return this.keyValue;
    }
}
