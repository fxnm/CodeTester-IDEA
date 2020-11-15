package de.fxnm.web.components.token;

public abstract class BaseToken {

    private final String token;

    public BaseToken(final String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
