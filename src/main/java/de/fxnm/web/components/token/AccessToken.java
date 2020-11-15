package de.fxnm.web.components.token;

public class AccessToken extends BaseToken {

    public AccessToken(final String token) {
        super(token);
    }

    @Override
    public String getToken() {
        return "Bearer " + super.getToken();
    }
}
